package com.example.messaging.dto.request;

import lombok.Builder;

@Builder
public record LocationDataRequest(

        double latitude,
        double longitude
) {
}
