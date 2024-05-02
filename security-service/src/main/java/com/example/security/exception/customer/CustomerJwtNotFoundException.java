package com.example.security.exception.customer;

import io.jsonwebtoken.JwtException;

/**
 * Исключение, сигнализирующее о невозможности найти JWT токен клиента.
 * Может быть брошено в случае, если JWT токен клиента не был найден в системе.
 */
public class CustomerJwtNotFoundException extends JwtException {


    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public CustomerJwtNotFoundException(String message) {
        super(message);
    }
}
