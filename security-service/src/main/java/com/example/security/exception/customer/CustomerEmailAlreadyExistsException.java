package com.example.security.exception.customer;

import jakarta.persistence.EntityExistsException;

public class CustomerEmailAlreadyExistsException extends EntityExistsException {

    public CustomerEmailAlreadyExistsException(String message) {
        super(message);
    }
}
