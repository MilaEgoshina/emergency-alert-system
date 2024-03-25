package com.example.templ.dto.response;

import lombok.Builder;

@Builder
public record LocationDataResponse(

        double latitude,
        double longitude
) {
}
