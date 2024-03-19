package com.example.messaging.service;

import com.example.messaging.client.LinkShortenerClient;
import com.example.messaging.client.MessageTemplateClient;
import com.example.messaging.dto.kafka.MessageKafka;
import com.example.messaging.dto.kafka.MessageRecipientCollectionKafka;
import com.example.messaging.dto.request.MessageRequest;
import com.example.messaging.dto.response.*;
import com.example.messaging.entity.Message;
import com.example.messaging.exception.message.MessageNotFoundException;
import com.example.messaging.exception.templates.RecipientTemplateNotFoundException;
import com.example.messaging.mapper.MessageMapper;
import com.example.messaging.model.MessageState;
import com.example.messaging.repository.MessageHistoryRepository;
import com.example.messaging.repository.MessageRepository;
import com.example.messaging.use.ListUtil;
import com.example.messaging.use.ServiceInstanceTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final KafkaTemplate<String, MessageRecipientCollectionKafka> kafkaTemplate;
    private final MessageHistoryRepository messageHistoryRepository;
    private final MessageRepository messageRepository;
    private final MessageTemplateClient messageTemplateClient;
    private final LinkShortenerClient shortenerClient;
    private final NotificationSourceService sourceService;
    private final MessageMapper messageMapper;
    private final ServiceInstanceTracker instanceTracker;

    @Value("${spring.application.name}")
    private String serviceName;

    //название темы (topic) в Apache Kafka, на которую будут отправляться сообщения с информацией о списках получателей
    // (recipients) для рассылки уведомлений.
    @Value("${spring.kafka.topics.separator}")
    private String recipientCollectionDistributionTopic;

    /**
     * Метод используется для распределения уведомлений на основе шаблона и списка получателей.
     * @param clientId идентификатор отправителя сообщения
     * @param templateId идентификатор шаблона
     * @return в случае успешной отправки возвращается строка, уведомляющая пользователя о том, что сообщения были доставлены.
     */
    public String distributeMessages(Long clientId, Long templateId) {

        // получаем шаблон уведомления из внешнего сервиса MessageTemplateClient
        MessageTemplateResponse templateResponse = messageTemplateClient
                .receiveTemplateBySenderIdAndTemplateId(clientId, templateId)
                .getBody();

        // извлекаем список идентификаторов получателей из полученного шаблона.
        List<Long> recipients = templateResponse.recipients()
                .stream()
                .map(MessageRecipientResponse::id)
                .toList();
        if (recipients.size() == 0) {
            throw new RecipientTemplateNotFoundException(
                    sourceService.getMessage("template.recipients.not_found", templateId, clientId)
            );
        }

        // создаем историю шаблона
        MessageTemplateHistoryResponse templateHistoryResponse = messageTemplateClient
                .receiveMessageTemplateHistory(clientId, templateId)
                .getBody();

        // разбиваем список получателей на подсписки
        for (List<Long> recipient : splitRecipients(recipients)) {

            // отправляем каждый подсписок в Kafka
             MessageRecipientCollectionKafka collectionKafka =
                     new MessageRecipientCollectionKafka(recipient, templateHistoryResponse, clientId);
            kafkaTemplate.send(recipientCollectionDistributionTopic, collectionKafka);
        }

        return "Notifications has been sent.";
    }

    /**
     * Метод для создания нового сообщения на основе запроса MessageRequest.
     * @param request экземпляр объекта запроса MessageRequest
     * @return возвращает объект MessageResponse после преобразования сохраненного объекта Message
     */
    public MessageResponse createMessage(MessageRequest request) {
        return Optional.of(request)
                .map(messageMapper::toEntity) // преобразование объекта запроса в объект сообщения
                .map(message -> message.associateTemplate(request.messageTemplate().id())) // добавление идентификатора
                // истории шаблона
                .map(messageRepository::saveAndFlush) // сохранение сообщения в БД
                .map(message -> messageMapper.toResponse(message, messageTemplateClient))
                .orElseThrow();
    }

    /**
     * Метод используется для получения списка уведомлений, которые должны быть переданы на переработку(повторную отправку).
     * @param pendingSec время в секундах для поиска уведомлений в состоянии ожидания.
     * @param newSec  время в секундах для поиска новых уведомлений.
     * @param size максимальное количество уведомлений для возврата.
     * @return возвращает список объектов MessageKafka, которые используются для отправки сообщений через Kafka.
     */
    public List<MessageKafka> receiveMessagesForRebalancing(Long pendingSec, Long newSec, Integer size) {

        LocalDateTime now = LocalDateTime.now();
        return messageRepository.getMessagesByStateAndCreatedOn(
                        now.minus(pendingSec, SECONDS), now.minus(newSec, SECONDS), Pageable.ofSize(size)
                ).stream()// получение списка сообщений со статусом IN_PROGRESS и созданные в определенном
                // временном интервале (pendingSec и newSec).
                .map(message -> message.setMessageState(MessageState.IN_PROGRESS)) // обновление статуса этих уведомлений на IN_PROGRESS
                .map(Message::updateCreationTime) // обновление даты создания
                .map(messageRepository::saveAndFlush) // сохранение обновленного сообщения в БД
                .map(message -> messageMapper.toKafka(message, messageTemplateClient, shortenerClient)) //преобразование
                // этих уведомлений в объекты MessageKafka
                .toList();
    }
    public MessageHistoryResponse setMessageAsDelivered(Long clientId, Long messageId) {
        return setMessageAsExecutedWithState(clientId, messageId, MessageState.DELIVERED);
    }

    public MessageHistoryResponse setMessageAsFailed(Long clientId, Long messageId) {
        return setMessageAsExecutedWithState(clientId, messageId, MessageState.FAILED);
    }

    public MessageHistoryResponse setMessageAsInvalid(Long clientId, Long messageId) {
        return setMessageAsExecutedWithState(clientId, messageId, MessageState.INVALID);
    }

    /**
     * Метод, который используется для обновления статуса уведомления на IN_PROGRESS.
     * @param clientId идентификатор отправителя сообщения
     * @param messageId идентификатор сообщения
     * @return объект MessageResponse для ответа на запрос о предоставлении информации об отправленных сообщениях.
     */
    public MessageResponse setMessageAsInProgress(Long clientId, Long messageId) {
        return messageRepository.getByIdAndSenderId(messageId, clientId)
                .map(message -> message.setMessageState(MessageState.IN_PROGRESS))
                .map(messageRepository::saveAndFlush)
                .map(message -> messageMapper.toResponse(message, messageTemplateClient)) //преобразование обновленного
                // сообщения в MessageResponse
                .orElseThrow(() -> new MessageNotFoundException(
                        sourceService.getMessage("message.not_found", messageId, clientId)
                ));
    }

    /**
     * Метод, который используется для обновления статуса уведомления на RESEND_QUEUED.
     * @param clientId идентификатор отправителя сообщения
     * @param messageId идентификатор сообщения
     * @return объект MessageResponse для ответа на запрос о предоставлении информации об отправленных сообщениях.
     */
    public MessageResponse setMessageAsResending(Long clientId, Long messageId) {
        return messageRepository.getByIdAndSenderId(messageId, clientId)
                .map(Message::incrementRetryCount)
                .map(message -> message.setMessageState(MessageState.RESEND_QUEUED))
                .map(messageRepository::saveAndFlush)
                .map(message -> messageMapper.toResponse(message, messageTemplateClient))
                .orElseThrow(() -> new MessageNotFoundException(
                        sourceService.getMessage("message.not_found", messageId, clientId)
                ));
    }

    /**
     * Метод, который используется для обновления статуса уведомления на заданный статус.
     * @param clientId идентификатор отправителя сообщения
     * @param messageId идентификатор сообщения
     * @param state заданный статус сообщения
     * @return возвращает объект MessageHistoryResponse для ответа на запрос об истории отправленных сообщений
     */
    private MessageHistoryResponse setMessageAsExecutedWithState(
            Long clientId, Long messageId,
            MessageState state
    ) {
        return messageRepository.getByIdAndSenderId(messageId, clientId) //поиск сообщения по его id и clientId
                .map(message -> {
                    messageRepository.delete(message); // удаление найденного сообщения из БД
                    return message;
                })
                .map(messageMapper::toHistory)
                .map(messageHistory -> messageHistory.setMessageState(state)) //обновление статуса на заданный в аргументах метода
                .map(messageHistoryRepository::saveAndFlush) // сохранение обновленного статуса и сообщения в БД
                .map(messageMapper::toResponse) // преобразование в объект MessageHistoryResponse для хранения истории об отправки
                // сообщений и их состояний
                .orElseThrow(() -> new MessageNotFoundException(
                        sourceService.getMessage("message.not_found", messageId, clientId)
                ));
    }

    private List<List<Long>> splitRecipients(List<Long> list) {
        return ListUtil.separator(list, instanceTracker.getRunningInstanceCount(serviceName));
    }

}
