package com.example.messaging.config;

import com.example.messaging.dto.kafka.MessageRecipientCollectionKafka;
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
 * Класс, который является конфигурационным классом для настройки потребителя (consumer) Apache Kafka.
 */
@Configuration
public class CustomKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers; // URL-адреса брокеров Kafka, к которым будет подключаться потребитель.

    /**
     * Метод создает и возвращает Map с конфигурационными свойствами для потребителя Kafka.
     */
    public Map<String, Object> kafkaConsumerConfig() {
        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers); // список брокеров Kafka для подключения
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); //  класс для десериализации ключей сообщений
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); // класс для обработки ошибки десериализации
        consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class); // класс для десериализации значений сообщений
        consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.messaging.dto.kafka"); // список доверенных пакетов, из которых можно десериализовать объекты.
        return consumerProperties;
    }

    /**
     * Метод, который создает и возвращает ConsumerFactory для создания экземпляров потребителей Kafka.
     * @return экземпляр ConsumerFactory, который создает экземпляры потребителей Kafka.
     */
    @Bean
    public ConsumerFactory<String, MessageRecipientCollectionKafka> kafkaConsumerFactory() {

        // реализация ConsumerFactory, которая создает экземпляры потребителей Kafka.
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig());
    }

    /**
     * Метод для обработки сообщений из Kafka в нескольких потоках.
     * @return возвращает KafkaListenerContainerFactory, которая управляет потоками потребителей Kafka и обработчиками сообщений.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MessageRecipientCollectionKafka>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageRecipientCollectionKafka> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(kafkaConsumerFactory());
        return containerFactory;
    }
}
