package com.example.sender.config;
import com.example.sender.dto.kafka.MessageKafka;
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
 * Класс для настройки конфигурацит потребителя Kafka в Spring, включая адреса брокеров, десериализацию ключей
 * и значений сообщений, а также создает фабрики потребителей и контейнеров слушателей.
 */
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Метод, который создает и возвращает Map с конфигурацией потребителя.
     * @return Map<String, Object> с конфигурацией потребителя.
     */
    public Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); // Адреса брокеров Kafka
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); //Класс для десериализации ключей сообщений
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); //Класс для десериализации значений сообщений
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class); //класс для десериализации значений сообщений - JsonDeserializer
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.company.messaging.dto"); //Пакеты, которым доверяет JsonDeserializer для десериализации объектов.
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MessageKafka.class); //Класс, который используется для десериализации значений сообщений по умолчанию.
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); //Флаг, указывающий, следует ли использовать информацию о типе из заголовков сообщений.
        return props;
    }

    /**
     * Метод, который создает и возвращает фабрику потребителей
     * @return ConsumerFactory<String, MessageKafka> фабрика потребителей
     */
    @Bean
    public ConsumerFactory<String, MessageKafka> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    /**
     * Метод, который создает и возвращает фабрику контейнеров слушателей.
     * @return контейнеры слушателей
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MessageKafka>> kafkaListenerContainerFactory() {

        //Конкретная реализация фабрики контейнеров слушателей, которая обеспечивает конкурентное потребление сообщений из топиков Kafka.
        ConcurrentKafkaListenerContainerFactory<String, MessageKafka> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
