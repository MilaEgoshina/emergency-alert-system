package com.example.security.exception.authtoken;

import io.jsonwebtoken.JwtException;

/**
 * Исключение, сигнализирующее о неудачной проверке токена аутентификации.
 * Может быть брошено в случае, если токен аутентификации не прошел проверку или не был распознан.
 */
public class FailedAuthTokenException extends JwtException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public FailedAuthTokenException(String message) {
        super(message);
    }
}
