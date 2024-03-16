package com.example.messaging.mapper;

import com.example.messaging.client.LinkShortenerClient;
import com.example.messaging.client.MessageTemplateClient;
import com.example.messaging.dto.kafka.MessageKafka;
import com.example.messaging.dto.request.MessageRequest;
import com.example.messaging.dto.response.MessageHistoryResponse;
import com.example.messaging.dto.response.MessageResponse;
import com.example.messaging.entity.Message;
import com.example.messaging.entity.MessageLog;
import org.mapstruct.Mapper;
import org.mapstruct.Context;
import org.mapstruct.Mapping;

import java.util.Map;

/**
 *
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<Message, MessageRequest, MessageResponse>{

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "messageState", ignore = true)
    @Mapping(target = "retryCount", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "templateId", ignore = true)
    Message toEntity(MessageRequest messageRequest);

    @Mapping(target = "template", expression = "java(messageTemplateClient.receiveMessageTemplateHistory(message.getSenderId(), message.getTemplateId()).getBody())")
    MessageResponse toDTO(Message message, @Context MessageTemplateClient messageTemplateClient);

    @Mapping(target = "template", expression = "java(messageTemplateClient.receiveMessageTemplateHistory(message.getSenderId(), message.getTemplateId()).getBody())")
    @Mapping(target = "linkOptions", expression = "java(shortenerClient.getShorterLink(template.responseId()).getBody().linkOptions())")
    MessageKafka toKafka(Message message, @Context MessageTemplateClient messageTemplateClient,
                            @Context LinkShortenerClient shortenerClient);

    @Mapping(target = "linkOptions", expression = "java(linkOptions)")
    MessageKafka toKafka(MessageResponse messageResponse, @Context Map<String, String> linkOptions);

    @Mapping(target = "id", ignore = true)
    MessageLog toHistory(Message message);

    MessageHistoryResponse toDTO(MessageLog messageLog);

}
