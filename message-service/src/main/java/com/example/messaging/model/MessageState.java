package com.example.messaging.model;

import lombok.AllArgsConstructor;

/**
 * Класс - перечисление для обозначений различных состояний, в которых может находиться уведомление (message).
 */
@AllArgsConstructor
public enum MessageState implements CodedEntity{

    CREATED("C"), // новое, только что созданное, уведомление
    IN_PROGRESS("P"), // уведомление в процессе отправки
    DELIVERED("D"), // успешно доставлено
    RESEND_QUEUED("R"), // в очереди на повторную отправку
    FAILED("F"), // обнаружено определенное количество ошибок при отправке
    INVALID("I"); // невозможно отправить вследствие какой - либо ошибки

    private final String identifier;


    @Override
    public String getIdentifier() {
        return identifier;
    }
}
