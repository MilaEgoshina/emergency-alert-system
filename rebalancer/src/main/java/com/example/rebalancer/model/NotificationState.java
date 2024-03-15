package com.example.rebalancer.model;

/**
 * Класс - перечисление для обозначений различных состояний, в которых может находиться уведомление (notification).
 */
public enum NotificationState {

    CREATED("C"), // новое, только что созданное, уведомление
    IN_PROGRESS("P"), // уведомление в процессе отправки
    DELIVERED("D"), // успешно доставлено
    RESEND_QUEUED("R"), // в очереди на повторную отправку
    FAILED("F"), // обнаружено определенное количество ошибок при отправке
    INVALID("I"); // невозможно отправить вследствие какой - либо ошибки

    private final String code;

    NotificationState(String code) {
        this.code = code;
    }
}
