package com.example.recipient.exception;

import jakarta.persistence.EntityNotFoundException;

// выбрасывается, когда получатель не найден в базе данных.
public class RecipientEntityNotFoundException extends EntityNotFoundException {

    public RecipientEntityNotFoundException(String message){

        super(message);
    }

}
