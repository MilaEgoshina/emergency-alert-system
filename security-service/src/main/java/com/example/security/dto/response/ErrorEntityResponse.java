package com.example.security.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ErrorEntityResponse(

        String errorMessage, // Строка с сообщением об ошибке.
        String errorDescription, // Строка с описанием ошибки.
        Integer errorCode, // Целочисленное значение, представляющее код ошибки.
        ZonedDateTime errorTimestamp // Поле типа ZonedDateTime, хранящее метку времени ошибки.
) {
}
