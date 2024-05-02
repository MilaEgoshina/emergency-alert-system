package com.example.templ.exception.templatexcpetions;

import jakarta.persistence.EntityNotFoundException;

/**
 * Исключение, сигнализирующее о том, что шаблон не найден.
 */
public class TemplateEntityNotFoundException extends EntityNotFoundException {

    /**
     * Создает новое исключение с заданным сообщением об ошибке.
     * @param message сообщение об ошибке
     */
    public TemplateEntityNotFoundException(String message){

        super(message);
    }
}
