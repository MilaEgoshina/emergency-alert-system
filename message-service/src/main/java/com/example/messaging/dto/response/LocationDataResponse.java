package com.example.messaging.dto.response;

import lombok.Builder;

@Builder
public record LocationDataResponse(

        double latitude,
        double longitude
) {
}
