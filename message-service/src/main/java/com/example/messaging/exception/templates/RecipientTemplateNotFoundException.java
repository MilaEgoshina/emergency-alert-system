package com.example.messaging.exception.templates;

import jakarta.persistence.EntityNotFoundException;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему шаблону получателя сообщений.
 */
public class RecipientTemplateNotFoundException extends EntityNotFoundException {

    public RecipientTemplateNotFoundException(String message){

        super(message);
    }
}
