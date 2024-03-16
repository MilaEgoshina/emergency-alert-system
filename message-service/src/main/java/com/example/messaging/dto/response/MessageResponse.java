package com.example.messaging.dto.response;

import com.example.messaging.model.MessageState;
import com.example.messaging.model.MessageWay;
import lombok.Builder;

import java.time.LocalDateTime;


/**
 * Класс - record, который представляет собой объект передачи данных для ответов, содержащих информацию об отправленных сообщениях.
 */
@Builder
public record MessageResponse(

        Long id, // уникальный идентификатор сообщения.
        MessageWay messageWay, // способ отправки сообщения (SMS, электронная почта и т.д.).
        String credential, // учетные данные, необходимые для отправки сообщения
        MessageState messageState, // текущее состояние сообщения (создано, отправлено, доставлено и т.д.).
        Integer retryCount, // количество попыток отправки сообщения.
        LocalDateTime createdOn, // дата и время создания сообщения.
        MessageTemplateHistoryResponse messageTemplate, // объект, содержащий информацию о шаблоне сообщения
        Long senderId // идентификатор отправителя сообщения.
) {
}
