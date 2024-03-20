package com.example.security.exception.customer;

import io.jsonwebtoken.JwtException;

public class CustomerJwtNotFoundException extends JwtException {

    public CustomerJwtNotFoundException(String message) {
        super(message);
    }
}
