package com.example.messaging.exception.message;

import jakarta.persistence.EntityNotFoundException;

public class MessageMapperNotFoundException extends EntityNotFoundException {

    public MessageMapperNotFoundException(String message){

        super(message);
    }
}
