package com.example.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Класс - record, который предназначен для упрощения передачи и хранения данных,
 * связанных с безопасностью (электронная почта и пароль).
 */
public record SecurityServiceRequest(

        @NotNull(message = "{auth.email.not_null}") @Email(message = "{auth.email.invalid}")
        String email,

        @NotNull(message = "{auth.password.not_null}") @Size(min = 5, max = 25, message = "{auth.password.size}")
        String password
) {
}
