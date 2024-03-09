package com.example.recipient.dto.kafka;

import lombok.Builder;

/**
 * Класс - record,
 */
@Builder
public record RecipientTemplateKafkaRecord(
        Long recipientId,
        Long templateId,
        Action action) {
}
