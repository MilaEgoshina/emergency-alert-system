package com.example.messaging.mapper;

import com.example.messaging.dto.request.MessageRequest;
import com.example.messaging.dto.response.MessageResponse;
import com.example.messaging.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<Message, MessageRequest, MessageResponse>{

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "retryAttempts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "templateHistoryId", ignore = true)
    Message toEntity(MessageRequest messageRequest);

    @Mapping(target = "template", expression = "java(templateClient.getTemplateHistory(notification.getClientId(), notification.getTemplateHistoryId()).getBody())")
    NotificationResponse mapToResponse(Notification notification, @Context TemplateClient templateClient);

    @Mapping(target = "template", expression = "java(templateClient.getTemplateHistory(notification.getClientId(), notification.getTemplateHistoryId()).getBody())")
    @Mapping(target = "urlOptionMap", expression = "java(shortenerClient.generate(template.responseId()).getBody().urlOptionMap())")
    NotificationKafka mapToKafka(Notification notification, @Context TemplateClient templateClient, @Context ShortenerClient shortenerClient);

    @Mapping(target = "urlOptionMap", expression = "java(urlOptionMap)")
    NotificationKafka mapToKafka(NotificationResponse notificationResponse, @Context Map<String, String> urlOptionMap);

    @Mapping(target = "id", ignore = true)
    NotificationHistory mapToHistory(Notification notification);

    NotificationHistoryResponse mapToResponse(NotificationHistory notificationHistory);

}
