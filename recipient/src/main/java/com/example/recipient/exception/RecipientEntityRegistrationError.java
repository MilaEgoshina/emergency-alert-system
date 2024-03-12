package com.example.recipient.exception;

public class RecipientEntityRegistrationError extends RuntimeException{

    // выбрасывается, когда произошла ошибка при регистрации получателя
    public RecipientEntityRegistrationError(String message){

        super(message);
    }
}
