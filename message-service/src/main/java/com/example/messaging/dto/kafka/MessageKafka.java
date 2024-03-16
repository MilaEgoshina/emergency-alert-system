package com.example.messaging.dto.kafka;

import com.example.messaging.dto.response.MessageTemplateHistoryResponse;
import com.example.messaging.model.MessageState;
import com.example.messaging.model.MessageWay;

import java.util.Map;


/**
 * Класс - record, который используется для предоставления информации о сообщениях, отправленных через Apache Kafka.
 */
public record MessageKafka(

        Long messageId, // уникальный идентификатор сообщения
        MessageWay messageWay, // способ отправки сообщения
        String credential, //  учетные данные для отправки сообщения
        MessageState messageState, // статус сообщения
        Integer retryCount, // количество попыток повторной отправки нотификации
        MessageTemplateHistoryResponse messageTemplate, // информация о шаблоне сообщения
        Long senderId, // идентификатор отправителя
        Map<String, String> urlOptions // словарь (отображение), где ключом является имя опции,
        // а значением - значение этой опции для данного сообщения.
) {
}
