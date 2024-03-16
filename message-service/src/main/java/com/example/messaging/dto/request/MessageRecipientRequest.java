package com.example.messaging.dto.request;

import lombok.Builder;

/**
 * Класс -record, который используется для составления запроса с информацией о получателе.
 */
@Builder
public record MessageRecipientRequest(

        String name, // имя получателя
        String email, // емайл получателя
        String phoneNumber, // телефон получателя
        String telegramId, // идентификатор в телеграмме у получателя
        LocationDataRequest locationData //географическая информация о получателе
) {
}
