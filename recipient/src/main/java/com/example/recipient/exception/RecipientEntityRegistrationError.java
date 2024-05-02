package com.example.recipient.exception;

/**
 * Пользовательское исключение, которое выбрасывается, когда произошла ошибка при регистрации получателя
 */
public class RecipientEntityRegistrationError extends RuntimeException{

    public RecipientEntityRegistrationError(String message){

        super(message);
    }
}
