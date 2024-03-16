package com.example.messaging.dto.response;

import lombok.Builder;

/**
 * Класс - record, используется для представления и передачи информации о получателях сообщений между различными
 * компонентами системы
 */
@Builder
public record MessageRecipientResponse(

        Long id, //  уникальный идентификатор получателя сообщения
        String name, // имя получателя.
        String email, // адрес электронной почты получателя.
        String phoneNumber, // номер телефона получателя.
        String telegramId, //  идентификатор получателя в мессенджере Telegram.
        LocationDataResponse locationData // объект, содержащий информацию о местоположении получателя
) {
}
