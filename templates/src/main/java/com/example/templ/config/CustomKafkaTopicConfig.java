package com.example.templ.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Класс настройки пользовательской конфигурации Kafka тем.
 */
@Configuration
public class CustomKafkaTopicConfig {

    @Value("${spring.kafka.partitions}")
    private Integer partitions;

    @Value("${spring.kafka.topics.recipient-update}")
    private String recipientUpdateTopic;

    /**
     * Метод для создания темы Kafka для обновления получателей.
     *
     * @return Новая тема Kafka для обновления получателей.
     */
    @Bean
    public NewTopic recipientUpdateTopic() {
        return TopicBuilder.name(recipientUpdateTopic)
                .partitions(partitions)
                .build();
    }
}
