package com.example.recipient.listener;

import com.example.recipient.dto.kafka.Action;
import com.example.recipient.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.recipient.entity.TemplateEntity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 *
 */
@RequiredArgsConstructor
public class TemplateEntityListener {

    private final KafkaTemplate<String, RecipientTemplateKafkaRecord> kafkaTemplate;

    public TemplateEntityListener(){
        kafkaTemplate = new KafkaTemplate<>(null);
    }
    @Value("${spring.kafka.topics.template-update}")
    private String topicName;

    @PostRemove
    public void handleRemoval(TemplateEntity templateEntity) {
        sendEvent(templateEntity, Action.REMOVE);
    }

    @PostPersist
    public void handleCreation(TemplateEntity templateEntity) {
        sendEvent(templateEntity, Action.PERSISTS);
    }

    private void sendEvent(TemplateEntity templateEntity, Action action) {
        kafkaTemplate.send(
                topicName,
                        RecipientTemplateKafkaRecord.builder()
                        .recipientId(templateEntity.getRecipientEntity().getId())
                        .templateId(templateEntity.getTemplateId())
                        .action(action)
                                .build()
        );
    }
}
