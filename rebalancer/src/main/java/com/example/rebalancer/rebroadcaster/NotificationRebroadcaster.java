package com.example.rebalancer.rebroadcaster;

import com.example.rebalancer.dto.kafka.NotificationMessageKafka;
import com.example.rebalancer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Класс, который выполняет повторную отправку уведомлений, полученных из сервиса NotificationService,
 * в различные каналы связи (телефон, электронная почта, телеграмм).
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificationRebroadcaster {

    @Value("${spring.kafka.topics.notifications.phone}")
    private String phoneNotificationTopic; // Поле для хранения темы Kafka для отправки уведомлений на телефон.

    @Value("${spring.kafka.topics.notifications.email}")
    private String emailNotificationTopic; // Поле для хранения темы Kafka для отправки уведомлений на электронную почту.

    @Value("${spring.kafka.topics.notifications.telegram}")
    private String messengerNotificationTopic; // Поле для хранения темы Kafka для отправки уведомлений в телеграмм.

    @Value("${rebroadcaster.pending-threshold}")
    private Long pendingThreshold; // Пороговое значение для "ожидающих" уведомлений.

    @Value("${rebroadcaster.new-threshold}")
    private Long newThreshold; // Пороговое значения для новых уведомлений.

    @Value("${rebroadcaster.fetch-limit}")
    private Integer fetchLimit; // Лимит количества уведомлений, получаемых за раз.

    private final NotificationService notificationService;

    private final KafkaTemplate<String, NotificationMessageKafka> messageKafkaTemplate; // Шаблон Kafka для отправки сообщений.

    /**
     * Метод выполняет повторную отправку уведомлений.
     * Метод будет запускаться периодически с фиксированной задержкой в 5000 миллисекунд (5 секунд).
     * То есть каждые 5 секунд метод будет выполняться.
     */
    @Scheduled(fixedDelay  = 5000)
    private void rebroadcastNotifications() {
        List<NotificationMessageKafka> notifications = notificationService.fetchNotificationsForRebalancing(
                pendingThreshold, newThreshold, fetchLimit)
                .getBody();

        // Проверяет, что список уведомлений не пустой.
        if (notifications == null || notifications.isEmpty()) {
            return;
        }

        // Для каждого уведомления происходит отправка в соответствующий канал
        for (NotificationMessageKafka notification : notifications) {
            switch (notification.getNotificationWay()) {
                case PHONENUMBER -> messageKafkaTemplate.send(phoneNotificationTopic, notification);
                case EMAIL -> messageKafkaTemplate.send(emailNotificationTopic, notification);
                case TELEGRAM -> messageKafkaTemplate.send(messengerNotificationTopic, notification);
            }
        }
    }

}
