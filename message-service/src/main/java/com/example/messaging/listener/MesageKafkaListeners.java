package com.example.messaging.listener;

import com.example.messaging.client.LinkShortenerClient;
import com.example.messaging.client.MessageRecipientClient;
import com.example.messaging.dto.kafka.MessageKafka;
import com.example.messaging.dto.kafka.MessageRecipientCollectionKafka;
import com.example.messaging.dto.request.MessageRequest;
import com.example.messaging.dto.response.LinkResponse;
import com.example.messaging.dto.response.MessageRecipientResponse;
import com.example.messaging.dto.response.MessageResponse;
import com.example.messaging.dto.response.MessageTemplateHistoryResponse;
import com.example.messaging.mapper.MessageMapper;
import com.example.messaging.model.MessageWay;
import com.example.messaging.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;


@Component
@RequiredArgsConstructor
public class MesageKafkaListeners {


    private final KafkaTemplate<String, MessageKafka> kafkaTemplate;
    private final MessageService messageService;

    private final MessageRecipientClient messageRecipientClient;
    private final LinkShortenerClient shortenerClient;
    private final MessageMapper messageMapper;

    // имена топиков Kafka для уведомлений:
    @Value("${spring.kafka.topics.messages.email}")
    private String emailKafkaTopic;

    @Value("${spring.kafka.topics.messages.phone}")
    private String phoneKafkaTopic;

    @Value("${spring.kafka.topics.messages.telegram}")
    private String telegramKafkaTopic;


    /**
     *
     * @param recipientCollectionKafka объект MessageRecipientCollectionKafka, который содержит список идентификаторов получателей,
     * идентификатор клиента и информацию о шаблоне уведомления.
     */
    @KafkaListener(
            // прослушивание сообщения из топика, определенного в свойстве:
            topics = "#{ '${spring.kafka.topics.separator}' }",
            groupId = "emergency",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listener(MessageRecipientCollectionKafka recipientCollectionKafka) {

        // создание экземпляра ExecutorService с одним потоком для асинхронной обработки сообщения.
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = () -> {
            Long clientId = recipientCollectionKafka.senderId();
            MessageTemplateHistoryResponse templateHistoryResponse = recipientCollectionKafka.messageTemplateHistory();

            // для каждого recipientId из списка:
            for (Long recipientId : recipientCollectionKafka.recipientIds()) {
                MessageRecipientResponse recipientResponse;
                try {
                    // получение объекта MessageRecipientResponse от messageRecipientClient, содержащий информацию о получателе.
                    recipientResponse = messageRecipientClient.getBySenderIdAndRecipientId(clientId, recipientId)
                            .getBody();
                } catch (RuntimeException e) {
                    continue;
                }

                // если MessageRecipientResponse не удалось получить или он равен null, обработка переходит к следующему recipientId
                if (recipientResponse == null) {
                    continue;
                }

                LinkResponse linkResponse = shortenerClient.getShorterLink(templateHistoryResponse.responseId())
                        .getBody();

                // отправка сообщений по электронной почте
                sendMessageByCredential(recipientResponse::email, MessageWay.EMAIL, recipientResponse, clientId,
                        templateHistoryResponse, emailKafkaTopic, linkResponse);

                // отправка сообщений по телефону (смс)
                sendMessageByCredential(recipientResponse::phoneNumber, MessageWay.PHONENUMBER, recipientResponse,
                        clientId, templateHistoryResponse, phoneKafkaTopic, linkResponse);

                // //отправка сообщений по телеграмму
                sendMessageByCredential(recipientResponse::telegramId, MessageWay.TELEGRAM, recipientResponse, clientId,
                        templateHistoryResponse, telegramKafkaTopic, linkResponse);
            }
        };

        // Объект Runnable выполняется в отдельном потоке через executorService.execute(runnable),
        // а затем executorService останавливается.
        executorService.execute(runnable);
        executorService.shutdown();
    }


    /**
     * Метод для создания уведомлений с соответствующими данными и отправки их в топики Kafka для дальнейшей обработки и отправки получателям.
     * @param supplier  функциональный интерфейс, который извлекает электронный адрес, номер телефона или идентификатор
     * Телеграма из MessageRecipientResponse.
     * @param way способ отправки уведомления.
     * @param recipientResponse объект, содержащий информацию о получателе.
     * @param clientId идентификатор отправителя.
     * @param templateHistoryResponse информация о шаблоне уведомления.
     * @param topic имя топика Kafka для отправки уведомления.
     * @param linkResponse объект, содержащий сокращенные ссылки для шаблона уведомления.
     */
    private void sendMessageByCredential(
                                               Supplier<String> supplier,
                                               MessageWay way,
                                               MessageRecipientResponse recipientResponse,
                                               Long clientId,
                                               MessageTemplateHistoryResponse templateHistoryResponse,
                                               String topic,
                                               LinkResponse linkResponse
    ) {

        String credential = supplier.get();
        if (credential != null) {
            Long messageId;
            try {
                // создания нового уведомления и получения его идентификатора
                messageId = messageService.createMessage(
                        MessageRequest.builder() // создание объекта на запрос отправки сообщения - MessageRequest
                                .messageWay(way)
                                .credential(credential)
                                .messageTemplate(templateHistoryResponse)
                                .recipientId(recipientResponse.id())
                                .senderId(clientId)
                                .linkId(linkResponse.linkId())
                                .build()
                ).id();
            } catch (EntityNotFoundException e) {
                return;
            }
            // установка статуса сообщения как "ожидающее отправки".
            MessageResponse messageResponse = messageService.setMessageAsInProgress(clientId, messageId);
            MessageKafka messageKafka = messageMapper.toKafka(messageResponse, linkResponse.linkOptions());
            // отправка сообщения в соответствующий топик Kafka
            kafkaTemplate.send(topic, messageKafka);
        }
    }
}
