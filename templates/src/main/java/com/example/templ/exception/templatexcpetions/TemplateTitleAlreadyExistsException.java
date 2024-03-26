package com.example.templ.exception.templatexcpetions;

import jakarta.persistence.EntityExistsException;

public class TemplateTitleAlreadyExistsException extends EntityExistsException {

    public TemplateTitleAlreadyExistsException(String message){

        super(message);
    }
}
