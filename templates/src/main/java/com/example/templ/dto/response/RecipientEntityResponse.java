package com.example.templ.dto.response;

import lombok.Builder;

/**
 * Ответ, содержащий информацию о получателе сообщения.
 */
@Builder
public record RecipientEntityResponse(

        /**
         * Уникальный идентификатор получателя.
         */
        Long id,


        /**
         * Адрес электронной почты получателя.
         */
        String name,

        /**
         * Адрес электронной почты получателя.
         */
        String email,


        /**
         * Номер телефона получателя.
         */
        String phoneNumber,

        /**
         * Идентификатор Telegram получателя.
         */
        String telegramId,

        /**
         * Данные о местоположении получателя.
         */
        LocationDataResponse locationData
) {
}
