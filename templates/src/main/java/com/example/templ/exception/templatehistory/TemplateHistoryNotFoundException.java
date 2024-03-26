package com.example.templ.exception.templatehistory;

import jakarta.persistence.EntityNotFoundException;

public class TemplateHistoryNotFoundException extends EntityNotFoundException {

    public TemplateHistoryNotFoundException(String message){

        super(message);
    }
}
