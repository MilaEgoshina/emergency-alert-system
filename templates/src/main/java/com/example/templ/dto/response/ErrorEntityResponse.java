package com.example.templ.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ErrorEntityResponse(

        String errorMessage,
        String errorDescription,
        Integer errorCode,
        ZonedDateTime errorTimestamp

) {
}
