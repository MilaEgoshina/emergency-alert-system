package com.example.templ.dto.response;

import java.util.List;

public record TemplateEntityResponse(

        Long id,
        String templateTitle,
        String templateContent,
        String templateImage,
        List<RecipientResponse> recipientIds
) {
}
