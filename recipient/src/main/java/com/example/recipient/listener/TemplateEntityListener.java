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
 * Класс - слушатель сущности JPA, который реагирует на события создания и удаления сущности TemplateEntity
 */
@RequiredArgsConstructor
public class TemplateEntityListener {

    // используем KafkaTemplate для отправки сообщений, в которых содержится информация об изменениях в сущностях TemplateEntity,
    // в topicName
    private final KafkaTemplate<String, RecipientTemplateKafkaRecord> kafkaTemplate;
    //получаем имя темы Kafka topicName из файла свойств приложения.
    @Value("${spring.kafka.topics.template-update}")
    private String topicName;

    public TemplateEntityListener(){
        kafkaTemplate = new KafkaTemplate<>(null);
    }


    /**
     * Метод вызывается после удаления сущности TemplateEntity.
     * @param templateEntity TemplateEntity для удаления
     */
    @PostRemove
    public void handleRemoval(TemplateEntity templateEntity) {
        sendEvent(templateEntity, Action.REMOVE);
    }

    /**
     * Метод вызывается после сохранения новой сущности TemplateEntity.
     * @param templateEntity TemplateEntity для сохранения
     */
    @PostPersist
    public void handleCreation(TemplateEntity templateEntity) {
        sendEvent(templateEntity, Action.PERSISTS);
    }

    /**
     * Метод отправляет сообщения в тему Kafka topicName, используя шаблон KafkaTemplate.
     * @param templateEntity объект TemplateEntity, представляющий идентификатор шаблона и получателя.
     * @param action перечисление Action, указывающее операцию, которая была выполнена с объектом TemplateEntity
     * (создание или удаление).
     */
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
