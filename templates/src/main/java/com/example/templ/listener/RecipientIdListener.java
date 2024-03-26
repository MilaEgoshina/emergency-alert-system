package com.example.templ.listener;

import com.example.templ.dto.kafka.Actions;
import com.example.templ.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.templ.entity.RecipientId;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Класс - слушатель событий для сущности RecipientId.
 */
@RequiredArgsConstructor
public class RecipientIdListener {

    // отправка сообщения в Kafka при удалении или сохранении сущности RecipientId через kafkaTemplate.
    private final KafkaTemplate<String, RecipientTemplateKafkaRecord> kafkaTemplate;

    @Value("${spring.kafka.topics.recipient-update}")
    private String recipientUpdateTopic;

    // метод отправляет сообщение в топик Kafka для уведомления об удалении сущности RecipientId.
    @PostRemove
    public void postRemove(RecipientId recipientId) {
        sendKafkaEvent(recipientId, Actions.REMOVE);
    }

    // метод отправляет сообщение в топик Kafka для уведомления о сохранении сущности RecipientId.
    @PostPersist
    public void postPersist(RecipientId recipientId){
        sendKafkaEvent(recipientId, Actions.PERSISTS);
    }

    private void sendKafkaEvent(RecipientId recipientId, Actions actions){
        kafkaTemplate.send(
                recipientUpdateTopic,
                RecipientTemplateKafkaRecord.builder()
                        .recipientId(recipientId.getRecipientId())
                        .templateId(recipientId.getTemplateEntity().getId())
                        .actions(actions)
                        .build()
        );
    }
}

