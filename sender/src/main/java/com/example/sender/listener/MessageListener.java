package com.example.sender.listener;

import com.example.sender.client.NotificationServiceClient;
import com.example.sender.dto.kafka.MessageKafka;
import com.example.sender.dto.response.TemplateHistoryResponse;
import com.example.sender.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Slf4j
@Component
public class MessageListener {

    private final TelegramNotificationService messagingService; //сервис для отправки сообщений
    private final NotificationServiceClient notificationServiceClient; //клиент для обновления статуса сообщений

    private final Random random = new Random();

    @Value("${message.maxRetryAttempts}")
    private int maxRetryAttempts; // максимальное количество попыток повторной отправки уведомления.


    /**
     * Метод - слушатель для топика телеграм-уведомлений.
     * @param message представляет полученное сообщение - Kafka.
     */
    @KafkaListener(
            // метод будет прослушивать сообщения из темы Kafka, указанной в атрибуте topics
            topics = "#{'${spring.kafka.topics.messages.telegram}'}",
            groupId = "emergency",
            // фабрика контейнеров kafkaListenerContainerFactory для создания контейнера слушателя.
            containerFactory = "kafkaListenerContainerFactory"
    )
    private void consumeTelegramNotification(MessageKafka message) {
        // регистрация полученного сообщения или уведомления в журнале с помощью вспомогательного метода
        logNotification(message);
        Long clientId = message.clientId();
        Long notificationId = message.id();

        // проверяем, превышает ли количество попыток повторной отправки уведомления максимальное количество попыток повторной отправки
        if (message.retryAttempts() >= maxRetryAttempts) {
            notificationServiceClient.markNotificationAsError(clientId, notificationId);
        } // вызывается вспомогательный метод для случайного определения, следует ли имитировать ошибку при отправке уведомления.
        else if (shouldSimulateError()) {
            notificationServiceClient.markNotificationAsResending(clientId, notificationId);
        } // если не имитируется ошибка, из сообщения извлекаются данные получателя (recipient) и шаблона (template)
        else {
            String recipient = message.credential();
            TemplateHistoryResponse template = message.template();
            // отправка сообщения (уведомления)
            boolean sent = messagingService.sendNotification(recipient, template);
            if (sent) {
                notificationServiceClient.markNotificationAsSent(clientId, notificationId);
            } else {
                notificationServiceClient.markNotificationAsResending(clientId, notificationId);
            }
        }
    }

    @KafkaListener(
            topics = "#{'${spring.kafka.topics.messages.email}'}",
            groupId = "emergency",
            containerFactory = "kafkaListenerContainerFactory"
    )
    private void consumeEmailNotification(MessageKafka message) {
        logNotification(message);
        Long clientId = message.clientId();
        Long notificationId = message.id();

        if (message.retryAttempts() >= maxRetryAttempts) {
            notificationServiceClient.markNotificationAsError(clientId, notificationId);
        } else if (shouldSimulateError()) {
            notificationServiceClient.markNotificationAsResending(clientId, notificationId);
        } else {
            String recipient = message.credential();
            TemplateHistoryResponse template = message.template();
            boolean sent = messagingService.sendNotification(recipient, template);
            if (sent) {
                notificationServiceClient.markNotificationAsSent(clientId, notificationId);
            } else {
                notificationServiceClient.markNotificationAsResending(clientId, notificationId);
            }
        }
    }

    @KafkaListener(
            topics = "#{'${spring.kafka.topics.messages.phone}'}",
            groupId = "emergency",
            containerFactory = "kafkaListenerContainerFactory"
    )
    private void consumeSmsNotification(MessageKafka message) {
        logNotification(message);
        Long clientId = message.clientId();
        Long notificationId = message.id();

        if (message.retryAttempts() >= maxRetryAttempts) {
            notificationServiceClient.markNotificationAsError(clientId, notificationId);
        } else if (shouldSimulateError()) {
            notificationServiceClient.markNotificationAsResending(clientId, notificationId);
        } else {
            String recipient = message.credential();
            TemplateHistoryResponse template = message.template();
            boolean sent = messagingService.sendNotification(recipient, template);
            if (sent) {
                notificationServiceClient.markNotificationAsSent(clientId, notificationId);
            } else {
                notificationServiceClient.markNotificationAsResending(clientId, notificationId);
            }
        }
    }

    /**
     * Метод для логирования информации об уведомлении.
     * @param message представляет полученное сообщение - Kafka.
     */
    private void logNotification(MessageKafka message) {
        log.info(
                "Received {} notification for '{}', status={}, retryAttempts={}",
                message.channel(),
                message.credential(),
                message.state(),
                message.retryAttempts()
        );
    }

    private boolean shouldSimulateError() {
        return ThreadLocalRandom.current().nextInt(100) <= 33;
    }
}
