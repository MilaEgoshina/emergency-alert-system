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
    @Value("${spring.kafka.topics.splitter}")
    private String recipientCollectionDistributionTopic;

    /**
     * Метод используется для распределения уведомлений на основе шаблона и списка получателей.
     * @param clientId идентификатор клиента
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
     * @param pendingSec время сообщения в процессе ожидания отправки
     * @param newSec время на новую отправку сообщения
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

    private MessageHistoryResponse setMessageAsExecutedWithState(
            Long clientId, Long messageId,
            MessageState state
    ) {
        return messageRepository.getByIdAndSenderId(messageId, clientId)
                .map(message -> {
                    messageRepository.delete(message);
                    return message;
                })
                .map(messageMapper::toHistory)
                .map(messageHistory -> messageHistory.setMessageState(state))
                .map(messageHistoryRepository::saveAndFlush)
                .map(messageMapper::toResponse)
                .orElseThrow(() -> new MessageNotFoundException(
                        sourceService.getMessage("message.not_found", messageId, clientId)
                ));
    }

    private List<List<Long>> splitRecipients(List<Long> list) {
        return ListUtil.separator(list, instanceTracker.getRunningInstanceCount(serviceName));
    }

}
