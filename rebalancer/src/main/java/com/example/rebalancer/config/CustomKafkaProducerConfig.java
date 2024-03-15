package com.example.rebalancer.config;

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
 * Класс - конфигурация Kafka Producer для отправки сообщений в Apache Kafka
 */
@Configuration
public class CustomKafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

    /**
     * Метод создает и возвращает Map<String, Object>, который содержит конфигурационные свойства для Kafka Producer.
     * @return Map<String, Object>, который содержит конфигурационные свойства для Kafka Producer.
     */
    public Map<String, Object> kafkaProducerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class);
        return configProps;
    }

    /**
     * Метод создает и возвращает ProducerFactory<String, ?>, который используется для создания экземпляра Kafka Producer.
     * @return ProducerFactory<String, ?>, который используется для создания экземпляра Kafka Producer.
     */
    @Bean
    public ProducerFactory<String, ?> customKafkaProducerFactory() {
        // используем DefaultKafkaProducerFactory, который принимает конфигурационные свойства для настройки Kafka Producer.
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
    }

    /**
     * Метод обеспечивает отправку сообщений в темы Kafka, используя созданный Kafka Producer.
     * @return KafkaTemplate<String, ?>, который представляет высокоуровневый API для отправки сообщений в темы Kafka.
     */
    @Bean
    public KafkaTemplate<String, ?> customKafkaTemplate() {

        // используем ProducerFactory из метода customKafkaProducerFactory() для создания экземпляра Kafka Producer и
        // настройки KafkaTemplate.
        return new KafkaTemplate<>(customKafkaProducerFactory());
    }
}
