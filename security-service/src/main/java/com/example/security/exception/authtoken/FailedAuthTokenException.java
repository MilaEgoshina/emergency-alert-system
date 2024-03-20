package com.example.security.exception.authtoken;

import io.jsonwebtoken.JwtException;

public class FailedAuthTokenException extends JwtException {

    public FailedAuthTokenException(String message) {
        super(message);
    }
}
