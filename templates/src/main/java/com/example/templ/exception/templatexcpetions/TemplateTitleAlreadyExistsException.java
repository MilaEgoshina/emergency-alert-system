package com.example.templ.exception.templatexcpetions;

import jakarta.persistence.EntityExistsException;

/**
 * Исключение, которое бросается, когда заголовок шаблона уже существует в системе.
 */
public class TemplateTitleAlreadyExistsException extends EntityExistsException {

    /**
     * Конструктор с параметром для инициализации сообщения об ошибке.
     * @param message сообщение об ошибке
     */
    public TemplateTitleAlreadyExistsException(String message){

        super(message);
    }
}
