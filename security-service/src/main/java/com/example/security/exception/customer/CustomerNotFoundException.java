package com.example.security.exception.customer;

import jakarta.persistence.EntityNotFoundException;

public class CustomerNotFoundException extends EntityNotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
