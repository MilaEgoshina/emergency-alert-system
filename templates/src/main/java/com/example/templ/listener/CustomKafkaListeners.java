package com.example.templ.listener;

import com.example.templ.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.templ.mapper.RecipientIdMapper;
import com.example.templ.repository.RecipientIdRepository;
import com.example.templ.repository.TemplateEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * Класс служит для обработки сообщений из Kafka, связанных с обновлением или добавлением информации о получателях шаблонов,
 * и взаимодействует с соответствующими репозиториями для выполнения необходимых операций с данными.
 */
@Component
@RequiredArgsConstructor
public class CustomKafkaListeners {

    private final RecipientIdRepository recipientIdRepository;

    private final TemplateEntityRepository templateEntityRepository;

    private final RecipientIdMapper recipientIdMapper;

    /**
     * Метод слушает указанную тему Kafka (определенную в `spring.kafka.topics.template-update`) и обрабатывает сообщения,
     * получаемые из этой темы.
     *
     * @param templateKafka объект класса RecipientTemplateKafkaRecord, который представляет данные, полученные из Kafka.
     * @return объект класса CompletableFuture<Void> для представления будущего завершения какой-либо асинхронной операции.
     */
    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.template-update}")
    public CompletableFuture<Void> customListener(RecipientTemplateKafkaRecord templateKafka) {
        switch (templateKafka.actions()) {
            case REMOVE -> {
                // извлекается шаблон из репозитория
                templateEntityRepository.findById(templateKafka.templateId())
                        .map(template -> template.removeRecipient(templateKafka.recipientId()))
                        .ifPresent(templateEntityRepository::saveAndFlush); // обновленный шаблон сохраняется обратно в репозиторий.
            }
            case PERSISTS -> {
                //проверка, существует ли уже получатель для указанного шаблона
                if (!recipientIdRepository.checkIfRecipientExistsForTemplateIdAndRecipientId(
                        templateKafka.templateId(),
                        templateKafka.recipientId()
                )) {
                    // Если нет, то создается новая запись получателя в репозитории.
                    recipientIdRepository.save(recipientIdMapper.toEntity(templateKafka));
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
