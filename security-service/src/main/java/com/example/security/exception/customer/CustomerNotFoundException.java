package com.example.security.exception.customer;

import jakarta.persistence.EntityNotFoundException;

/**
 * Исключение, сигнализирующее о невозможности найти клиента.
 * Может быть брошено в случае, если клиент не найден в системе.
 */
public class CustomerNotFoundException extends EntityNotFoundException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
