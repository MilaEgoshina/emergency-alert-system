package com.example.templ.dto.kafka;

import lombok.Builder;

/**
 * Класс - record для передачи информации о получателе и шаблоне сообщения в системе Kafka.
 */
@Builder
public record RecipientTemplateKafkaRecord(

        Long recipientId,
        Long templateId,
        Actions actions // действия, которые должны быть выполнены для этого получателя по данному шаблону.
) {
}
