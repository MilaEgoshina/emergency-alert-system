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

        /**
         * Уникальный идентификатор ответа.
         */
        Long responseId,

        /**
         * Заголовок шаблона.
         */
        String header,

        /**
         * Детали шаблона.
         */
        String details,

        /**
         * URL ссылки.
         */
        String linkURL
) {
}
