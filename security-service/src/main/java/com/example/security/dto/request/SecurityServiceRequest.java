package com.example.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Класс - record, который используется для передачи данных при выполнении операций в сервисе безопасности
 * (при регистрации или аутентификации пользователя).
 */
public record SecurityServiceRequest(

        @NotNull(message = "{auth.email.not_null}") @Email(message = "{auth.email.invalid}")
        String email,

        @NotNull(message = "{auth.password.not_null}") @Size(min = 5, max = 25, message = "{auth.password.size}")
        String password
) {
}
