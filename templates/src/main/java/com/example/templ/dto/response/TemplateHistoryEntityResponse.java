package com.example.templ.dto.response;

import lombok.Builder;


/**
 * Ответ, содержащий информацию об истории шаблонов.
 */
@Builder
public record TemplateHistoryEntityResponse(

        /**
         * Уникальный идентификатор записи истории шаблона.
         */
        Long id,


        Long responseId,
        String header,
        String details,
        String linkURL
) {
}
