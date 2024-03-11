package com.example.recipient.listener;

import com.example.recipient.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.recipient.entity.RecipientEntity;
import com.example.recipient.entity.TemplateEntity;
import com.example.recipient.repository.RecipientEntityRepository;
import com.example.recipient.repository.TemplateEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * Класс представляет собой реализацию слушателя Apache Kafka для прослушивания сообщений из топика Kafka с именем
 * recipient-update и выполнения определенных действий в зависимости от содержимого сообщения.
 */

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    //репозитории для выполнения операций с базой данных:
    private final TemplateEntityRepository templateEntityRepository;
    private final RecipientEntityRepository recipientEntityRepository;

    @Transactional
    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.recipient-update}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )

    /**
     * Данный метод вызывается каждый раз, когда приходит новое сообщение в топик recipient-update
     * @param kafkaTemplate объект RecipientTemplateKafkaRecord, который является моделью данных,
     * представляющей сообщение Kafka.
     */
    public void listener(RecipientTemplateKafkaRecord kafkaTemplate) {
        if ("REMOVE".equals(kafkaTemplate.action())) {
            RecipientEntity recipient = recipientEntityRepository.findById(kafkaTemplate.recipientId()).orElse(null);
            if (recipient != null) {
                recipient.removeTemplate(kafkaTemplate.templateId());
                recipientEntityRepository.save(recipient);
            }
        } else if ("PERSISTS".equals(kafkaTemplate.action())) {
            boolean exists = templateEntityRepository.existsByTemplateEntityAndRecipientId(kafkaTemplate.templateId(),
                    kafkaTemplate.recipientId());
            if (!exists) {

                templateEntityRepository.save(
                        TemplateEntity.builder()
                                .recipientEntity(RecipientEntity.builder()
                                        .id(kafkaTemplate.recipientId())
                                        .build())
                                .templateId(kafkaTemplate.templateId())
                                .build()
                );
            }
        }
    }
}
