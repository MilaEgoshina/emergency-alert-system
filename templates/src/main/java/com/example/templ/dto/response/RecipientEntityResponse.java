package com.example.templ.dto.response;

import lombok.Builder;

@Builder
public record RecipientEntityResponse(

        Long id,
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        LocationDataResponse locationData
) {
}
