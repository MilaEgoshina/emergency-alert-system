package com.example.messaging.exception.message;

import jakarta.persistence.EntityNotFoundException;

/**
 * Исключение, которое возникает, когда не удается найти маппер для сообщения.
 */
public class MessageMapperNotFoundException extends EntityNotFoundException {

    /**
     * Конструктор с параметром сообщения.
     *
     * @param message сообщение об ошибке
     */
    public MessageMapperNotFoundException(String message){

        super(message);
    }
}
