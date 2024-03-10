package com.example.recipient.dto.response;

import lombok.Builder;

/**
 * Класс – record RecipientEntityResponse представляет ответ о получателе.
 */
@Builder
public record RecipientEntityResponse(

        Long id,

        String name,

        String email,

        String phoneNumber,

        String telegramId,

        LocationDataResponse locationDataResponse) {
}
