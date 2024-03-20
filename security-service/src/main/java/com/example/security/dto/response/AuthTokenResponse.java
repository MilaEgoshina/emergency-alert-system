package com.example.security.dto.response;

/**
 * Класс - record для передачи информации о сгенерированном токене (JWT) в ответе.
 */
public record AuthTokenResponse(

        String jwt // Строка, содержащая сгенерированный токен.
) {
}
