package com.example.security.exception.customer;

import org.springframework.security.authentication.BadCredentialsException;

public class CustomerBadCredentialsException extends BadCredentialsException {

    public CustomerBadCredentialsException(String msg) {
        super(msg);
    }
}
