package com.example.security.exception.customer;

import jakarta.persistence.EntityExistsException;

/**
 * Исключение, сигнализирующее о попытке создания клиента с уже существующим адресом электронной почты.
 * Может быть брошено в случае, если в процессе создания нового клиента обнаружено, что адрес электронной почты уже занят другим клиентом.
 */
public class CustomerEmailAlreadyExistsException extends EntityExistsException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public CustomerEmailAlreadyExistsException(String message) {
        super(message);
    }
}
