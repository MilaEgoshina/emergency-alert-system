package com.example.recipient.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

/**
 * Класс - record используется для единообразной обработки и отображения ошибок в REST API.
 */
@Builder
public record ErrorEntityResponse(

        String errorMessage, // Краткое описание ошибки
        String errorDescription, // Подробное описание ошибки
        int errorCode, // Код ошибки
        ZonedDateTime errorTimestamp) //Временная метка, указывающая, когда произошла ошибка
{
}
