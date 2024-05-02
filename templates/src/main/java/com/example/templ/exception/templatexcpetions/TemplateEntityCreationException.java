package com.example.templ.exception.templatexcpetions;

/**
 * Исключение, выбрасываемое при ошибке создания сущности шаблона.
 */
public class TemplateEntityCreationException extends RuntimeException{

    /**
     * Конструктор с одним параметром, принимающий сообщение об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public TemplateEntityCreationException(String message){

        super(message);
    }
}
