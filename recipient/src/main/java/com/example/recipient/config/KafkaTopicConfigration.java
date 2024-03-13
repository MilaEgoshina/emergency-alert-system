package com.example.recipient.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfigration {

    @Value("${spring.kafka.partitions}")
    private int partitionCount;

    @Value("${spring.kafka.topics.template-update}")
    private String templateUpdateTopicName;

    @Bean
    public NewTopic templateUpdateTopic() {
        return TopicBuilder.name(templateUpdateTopicName)
                .partitions(partitionCount)
                .build();
    }
}
