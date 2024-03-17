package com.example.messaging.exception.message;

import jakarta.persistence.EntityNotFoundException;

public class MessageNotFoundException extends EntityNotFoundException {

    public MessageNotFoundException(String message){

        super(message);
    }
}
