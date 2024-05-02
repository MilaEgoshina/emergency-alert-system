package com.example.templ.exception.templatehistory;

import jakarta.persistence.EntityNotFoundException;

/**
 * Исключение, сигнализирующее о том, что история шаблона не найдена.
 */
public class TemplateHistoryNotFoundException extends EntityNotFoundException {

    /**
     * Создает новое исключение с заданным сообщением об ошибке.
     * @param message сообщение об ошибке
     */
    public TemplateHistoryNotFoundException(String message){

        super(message);
    }
}
