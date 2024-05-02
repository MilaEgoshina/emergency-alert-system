package com.example.messaging.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

/**
 * Класс, представляющий объект ответа с информацией об ошибке.
 *
 * errorMessage - Краткое описание ошибки
 *
 * errorDescription - Подробное описание ошибки
 *
 * errorCode - Код ошибки
 *
 * errorTimestamp - Временная метка, указывающая, когда произошла ошибка
 */
@Builder
public record ErrorEntityResponse(

        String errorMessage, // Краткое описание ошибки

        String errorDescription, // Подробное описание ошибки

        int errorCode, // Код ошибки

        ZonedDateTime errorTimestamp) //Временная метка, указывающая, когда произошла ошибка
{
}
