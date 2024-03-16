package com.example.messaging.dto.request;

import com.example.messaging.dto.response.MessageTemplateHistoryResponse;
import com.example.messaging.model.MessageWay;
import lombok.Builder;

/**
 * Класс - record, который используется для создания объектов запроса на отправку уведомлений.
 */
@Builder
public record MessageRequest(

        MessageWay messageWay, // способ отправки сообщения.
        String credential, // учетные данные для отправки уведомления.
        MessageTemplateHistoryResponse messageTemplate, // информация о шаблоне уведомления.
        Long recipientId, // идентификатор получателя.
        Long senderId, // идентификатор отправителя.
        Long linkId // идентификатор URL.
) {
}
