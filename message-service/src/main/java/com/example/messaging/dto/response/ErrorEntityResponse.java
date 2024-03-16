package com.example.messaging.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ErrorEntityResponse(

        String errorMessage, // Краткое описание ошибки

        String errorDescription, // Подробное описание ошибки

        int errorCode, // Код ошибки

        ZonedDateTime errorTimestamp) //Временная метка, указывающая, когда произошла ошибка
{
}
