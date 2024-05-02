package com.example.messaging.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Класс, который является конфигурационным классом для создания топиков (topics) в Apache Kafka
 */
@Configuration
public class CustomKafkaTopicConfig {

    @Value("${spring.kafka.partitions}")
    private Integer partitions; // количество разделов (partitions) для создаваемых топиков.

    // имена топиков, которые будут созданы:

    @Value("${spring.kafka.topics.messages.email}")
    private String emailKafkaTopic; // Наименование топика для сообщений по электронной почте.

    @Value("${spring.kafka.topics.messages.phone}")
    private String phoneKafkaTopic;  // Наименование топика для сообщений по телефону.

    @Value("${spring.kafka.topics.messages.telegram}")
    private String telegramKafkaTopic; // Наименование топика для сообщений в Telegram.

    @Value("${spring.kafka.topics.separator}")
    private String separatorKafkaTopic; // Наименование топика для разделителя.

    /**
     * Метод создания топика для сообщений по электронной почте.
     * @return созданный топик для сообщений по электронной почте
     */
    @Bean
    public NewTopic createEmailMessageTopic() {
        return TopicBuilder.name(emailKafkaTopic) // создание имя топика Kafka.
                .partitions(partitions) // определение количества разделов для созданного топика.
                .build(); //  возвращает экземпляр NewTopic, который представляет новый топик в Kafka.
    }

    /**
     * Метод создания топика для сообщений по телефону.
     * @return созданный топик для сообщений по телефону
     */
    @Bean
    public NewTopic createPhoneMessageTopic() {
        return TopicBuilder.name(phoneKafkaTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    public NewTopic createTelegramMessageTopic() {
        return TopicBuilder.name(telegramKafkaTopic)
                .partitions(partitions)
                .build();
    }

    /**
     * Метод создания топика для сообщений в Telegram.
     * @return созданный топик для сообщений в Telegram
     */
    @Bean
    public NewTopic createSeparatorTopic() {
        return TopicBuilder.name(separatorKafkaTopic)
                .partitions(partitions)
                .build();
    }
}
