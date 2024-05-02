package com.example.recipient.config;


import com.example.recipient.dto.kafka.RecipientTemplateKafkaRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для потребителя Kafka, который будет слушать сообщения, сериализованные в формате JSON.
 */
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Метод для получения свойств потребителя Kafka.
     *
     * @return свойства потребителя Kafka
     */
    @Bean
    public Map<String, Object> consumerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.template.dto.kafka");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.recipient.dto.kafka.RecipientTemplateKafkaRecord");
        properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return properties;
    }

    /**
     * Метод для создания фабрики потребителя Kafka.
     *
     * @return фабрика потребителя Kafka
     */
    @Bean
    public ConsumerFactory<String, RecipientTemplateKafkaRecord> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProperties());
    }

    /**
     * Метод для создания контейнера сообщений Kafka.
     *
     * @return контейнер сообщений Kafka
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RecipientTemplateKafkaRecord>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RecipientTemplateKafkaRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
