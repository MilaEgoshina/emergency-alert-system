package com.example.messaging.dto.kafka;


import com.example.messaging.dto.response.MessageTemplateHistoryResponse;

import java.util.List;

/**
 * Класс, который используется для передачи данных о получателях уведомлений через Apache Kafka.
 */
public record MessageRecipientCollectionKafka(

        List<Long> recipientIds, // список идентификаторов получателей
        MessageTemplateHistoryResponse messageTemplateHistory, //шаблон уведомления
        Long senderId // идентификатор отправителя

) {
}
