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

    private final TelegramNotificationService messagingService;
    private final NotificationServiceClient notificationServiceClient;

    private final Random random = new Random();

    @Value("${notification.maxRetryAttempts}")
    private int maxRetryAttempts;

    @KafkaListener(
            topics = "#{'${spring.kafka.topics.notifications.telegram}'}",
            groupId = "emergency",
            containerFactory = "kafkaListenerContainerFactory"
    )
    private void consumeTelegramNotification(MessageKafka message) {
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
            topics = "#{'${spring.kafka.topics.notifications.email}'}",
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
            topics = "#{'${spring.kafka.topics.notifications.sms}'}",
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
