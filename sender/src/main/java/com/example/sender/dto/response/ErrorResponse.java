package com.example.sender.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Класс для предоставления ответов с сообщениями об ошибках
 */
@Getter
public class ErrorResponse {
    private final String message; // сообщение об ошибке
    private final String description; // описание ошибки
    private final Integer code; // код ошибки
    private final LocalDateTime timestamp; // время возникновения ошибки

    private ErrorResponse(String message, String description, Integer code, LocalDateTime timestamp) {
        this.message = message;
        this.description = description;
        this.code = code;
        this.timestamp = timestamp;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

     static class ErrorResponseBuilder {
        private String message;
        private String description;
        private Integer code;
        private LocalDateTime timestamp;

        public ErrorResponseBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ErrorResponseBuilder setCode(Integer code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(message, description, code, timestamp);
        }
    }
}