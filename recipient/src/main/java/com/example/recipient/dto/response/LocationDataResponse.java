package com.example.recipient.dto.response;

import lombok.Builder;


/**
 * Класс - record, который предоставляет ответ сервера о местоположении получателя.
 */
@Builder
public record LocationDataResponse(

        double latitude,

        double longitude) {
}
