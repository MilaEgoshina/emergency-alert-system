package com.example.messaging.dto.response;

import com.example.messaging.model.MessageState;
import com.example.messaging.model.MessageWay;

import java.time.LocalDateTime;

/**
 * Класс, который используется для представления и передачи истории об отправленных сообщениях (и их состоянии) между
 * различными компонентами системы.
 */
public record MessageHistoryResponse(

        Long id, // уникальный идентификатор сообщения.
        MessageWay messageWay, // способ отправки сообщения
        String credential, // учетные данные, необходимые для отправки сообщения (данные для аутентификации).
        MessageState messageState, // текущее состояние сообщения (создано, отправлено, доставлено и т.д.).
        Integer retryCount, // количество попыток отправки сообщения.
        LocalDateTime createdOn, // дата и время создания сообщения.
        LocalDateTime executedOn, // дата и время отправки сообщения.
        MessageTemplateHistoryResponse messageTemplate, // объект, содержащий информацию о шаблоне сообщения
        Long senderId // идентификатор отправителя сообщения.
) {
}
