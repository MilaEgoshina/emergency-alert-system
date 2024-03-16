package com.example.messaging.dto.response;

import lombok.Builder;

import java.util.List;

/**
 * Класс - record, предоставляющий возможность передачи и хранения информации о шаблонах сообщений и связанных с ними получателях.
 */
@Builder
public record MessageTemplateResponse(

        Long id, // уникальный идентификатор шаблона сообщения.
        String subject, // заголовок сообщения.
        String body, //  содержимое сообщения.
        String mediaLink, // ссылка на медиа-контент, связанный с сообщением
        List<MessageRecipientResponse> recipients // список получателей сообщения, представленных объектами MessageRecipientResponse.
)
{
}
