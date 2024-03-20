package com.example.messaging.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


/**
 * Класс, который является конфигурационным классом для настройки производителя (producer) Apache Kafka.
 */
@Configuration
public class CustomKafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers; // URL-адреса брокеров Kafka, к которым будет подключаться производитель.

    /**
     * Метод для определения конфигурации производителя.
     * @return возвращает Map с конфигурационными свойствами для производителя Kafka.
     */
    public Map<String, Object> kafkaProducerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers); // список брокеров Kafka для подключения.
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // класс для сериализации ключей сообщений.
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // класс для сериализации значений сообщений.
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class); // класс, который определяет,
        // в какой раздел (partition) топика будет помещено сообщение. Здесь используется RoundRobinPartitioner,
        // который распределяет сообщения по разделам по принципу Round-Robin.
        return configProps;
    }

    /**
     * Метод, который создает и возвращает ProducerFactory для создания экземпляров производителей Kafka.
     * @return возвращает ProducerFactory для создания экземпляров производителей Kafka.
     */
    @Bean
    public ProducerFactory<String, ?> producerFactory() {

        // передача Map с конфигурацией, созданной в kafkaProducerConfig().
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
    }

    /**
     * Метод для отправки сообщений в Kafka.
     * @return возвращает KafkaTemplate, который предоставляет удобный интерфейс для отправки сообщений в Kafka.
     */
    @Bean
    public KafkaTemplate<String, ?> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
