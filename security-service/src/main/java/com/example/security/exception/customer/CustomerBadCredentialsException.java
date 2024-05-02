package com.example.security.exception.customer;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * Исключение, сигнализирующее о неверных учетных данных клиента.
 * Может быть брошено в случае, если клиент предоставил неправильные учетные данные при аутентификации.
 */
public class CustomerBadCredentialsException extends BadCredentialsException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param msg сообщение об ошибке
     */
    public CustomerBadCredentialsException(String msg) {
        super(msg);
    }
}
