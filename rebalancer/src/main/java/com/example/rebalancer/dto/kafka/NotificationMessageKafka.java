package com.example.rebalancer.dto.kafka;

import com.example.rebalancer.dto.response.ResponseEntity;
import com.example.rebalancer.model.NotificationState;
import com.example.rebalancer.model.NotificationWay;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * Класс - модель данных, которая используется для передачи информации об уведомлениях через Apache Kafka.
 */
@AllArgsConstructor
@Getter
public class NotificationMessageKafka {

    private final Long messageId; // Уникальный идентификатор сообщения
    private final NotificationWay notificationWay; // Enum, представляющий способ отправки уведомлений
    private final String authCredential; // Учетные данные, необходимые для отправки уведомления
    private final NotificationState state; // Enum, представляющий текущее состояние уведомления
    private final Integer retryCount; // Количество попыток повторной отправки уведомления в случае ошибки.
    private final LocalDateTime createdTimestamp; // Дата и время создания уведомления.
    private final ResponseEntity responseEntity; // Объект, содержащий информацию о самом уведомлении (заголовок, содержание, ссылка на изображение и т.д.).
    private final Long clientIdentifier; // Идентификатор клиента, для которого предназначено уведомление.
    private final Map<String, String> urlParams; // Карта параметров, которые могут быть использованы для построения URL-адресов, связанных с уведомлением.

}
