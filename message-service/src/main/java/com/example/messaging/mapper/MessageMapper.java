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
 * Интерфейс - маппера, который используется для преобразования (маппинга) объектов одного типа в объекты другого типа.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<Message, MessageRequest, MessageResponse>{

    /**
     * Метод, который преобразует MessageRequest в Message.
     * @param messageRequest запрос на создание/отправку сообщения
     * @return объект сущности Message
     */
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "messageState", ignore = true)
    @Mapping(target = "retryCount", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "templateId", ignore = true)
    Message toEntity(MessageRequest messageRequest);

    /**
     * Метод преобразует Message в MessageResponse.
     * @param message объект сущности сообщения для получателей
     * @param messageTemplateClient зависимость MessageTemplateClient, которая необходима для получения шаблона сообщения (template).
     * @return объект MessageResponse для ответа на запрос создания/отправки сообщения
     */
    @Mapping(target = "messageTemplate", expression = "java(messageTemplateClient.receiveMessageTemplateHistory(message.getSenderId(), message.getTemplateId()).getBody())")
    MessageResponse toResponse(Message message, @Context MessageTemplateClient messageTemplateClient);

    /**
     * Метод преобразует Message в MessageKafka, который используется для отправки сообщения в Kafka.
     * @param message объект сущности сообщения
     * @param messageTemplateClient зависимость, которая используется для получения шаблона уведомления (messageTemplateClient)
     * из внешнего сервиса.
     * @param shortenerClient зависимость, которая используется для генерации URL-опций на основе responseId из шаблона уведомления.
     * @return объект MessageKafka, который используется для отправки сообщения в Kafka.
     */
    @Mapping(target = "messageTemplate", expression = "java(messageTemplateClient.receiveMessageTemplateHistory(message.getSenderId(), message.getTemplateId()).getBody())")
    @Mapping(target = "linkOptions", expression = "java(shortenerClient.getShorterLink(messageTemplate.responseId()).getBody().linkOptions())")
    MessageKafka toKafka(Message message, @Context MessageTemplateClient messageTemplateClient,
                            @Context LinkShortenerClient shortenerClient);

    /**
     * Метод преобразует MessageResponse в MessageKafka, используя предоставленные параметры ссылок (linkOptions).
     * @param messageResponse ответ на запрос о создании или отправки уведомления.
     * @param linkOptions параметры ссылок
     * @return объект MessageKafka, который используется для отправки сообщения в Kafka.
     */
    @Mapping(target = "linkOptions", expression = "java(linkOptions)")
    MessageKafka toKafka(MessageResponse messageResponse, @Context Map<String, String> linkOptions);

    /**
     * Метод преобразует Message в MessageLog, который используется для ведения журнала истории сообщений.
     * @param message объект сущности сообщения для получателей
     * @return возвращает объект MessageLog, который используется для ведения журнала истории сообщений.
     */
    @Mapping(target = "id", ignore = true)
    MessageLog toHistory(Message message);

    /**
     * Метод преобразует MessageLog в MessageHistoryResponse, который используется для отображения истории сообщений.
     * @param messageLog объект MessageLog, который используется для ведения журнала истории сообщений.
     * @return возвращает объект MessageHistoryResponse, который отвечает на запрос об отображения истории сообщений.
     */
    MessageHistoryResponse toResponse(MessageLog messageLog);

}
