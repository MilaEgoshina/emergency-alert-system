package com.example.messaging.exception.message;

import jakarta.persistence.EntityNotFoundException;

/**
 * Исключение, которое выбрасывается, когда сообщение не найдено.
 */
public class MessageNotFoundException extends EntityNotFoundException {

    /**
     * Конструктор с параметром для инициализации сообщения об ошибке.
     * @param message сообщение об ошибке
     */
    public MessageNotFoundException(String message){

        super(message);
    }
}
