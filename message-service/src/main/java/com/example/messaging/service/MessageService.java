package com.example.messaging.service;

import com.example.messaging.client.LinkShortenerClient;
import com.example.messaging.client.MessageTemplateClient;
import com.example.messaging.dto.kafka.MessageRecipientCollectionKafka;
import com.example.messaging.mapper.MessageMapper;
import com.example.messaging.repository.MessageHistoryRepository;
import com.example.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final KafkaTemplate<String, MessageRecipientCollectionKafka> kafkaTemplate;
    private final MessageHistoryRepository messageHistoryRepository;
    private final MessageRepository messageRepository;
    private final MessageTemplateClient messageTemplateClient;
    private final LinkShortenerClient shortenerClient;
    private final NotificationSourceService sourceService;
    private final MessageMapper messageMapper;

}
