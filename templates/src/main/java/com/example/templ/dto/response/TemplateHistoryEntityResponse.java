package com.example.templ.dto.response;

import lombok.Builder;

@Builder
public record TemplateHistoryEntityResponse(

        Long id,
        Long responseId,
        String header,
        String details,
        String linkURL
) {
}
