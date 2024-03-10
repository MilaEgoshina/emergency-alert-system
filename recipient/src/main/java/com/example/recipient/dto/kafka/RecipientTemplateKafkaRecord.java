package com.example.recipient.dto.kafka;

import lombok.Builder;

/**
 * Класс - record, который используется для представления сообщения, отправляемого через Kafka.
 */
@Builder
public record RecipientTemplateKafkaRecord(
        Long recipientId,
        Long templateId,
        Action action) {
}
