package com.example.messaging.dto.response;

import lombok.Builder;

/**
 * Класс - record, который используется для создания объектов, содержащих информацию о шаблонах сообщений.
 */
@Builder
public record MessageTemplateHistoryResponse(

        Long id, // идентификатор шаблона сообщения
        Long responseId, // идентификатор ответа, связанного с шаблоном
        String subject, // тема или заголовок сообщения.
        String body, // текст или содержимое сообщения.
        String mediaLink // ссылка на медиа-контент, связанный с сообщением.
) {
}
