package com.example.messaging.exception.templates;

import jakarta.persistence.EntityNotFoundException;

public class RecipientTemplateNotFoundException extends EntityNotFoundException {

    public RecipientTemplateNotFoundException(String message){

        super(message);
    }
}
