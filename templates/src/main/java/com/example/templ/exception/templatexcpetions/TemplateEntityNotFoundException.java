package com.example.templ.exception.templatexcpetions;

import jakarta.persistence.EntityNotFoundException;

public class TemplateEntityNotFoundException extends EntityNotFoundException {

    public TemplateEntityNotFoundException(String message){

        super(message);
    }
}
