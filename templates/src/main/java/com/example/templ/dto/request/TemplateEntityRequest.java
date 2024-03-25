package com.example.templ.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;


/**
 * Класс для хранения данных, полученных из запроса, при создании или обновлении шаблона.
 */
@Builder
public record TemplateEntityRequest(

        @NotNull(message = "{template.title.not_null}") @Size(min = 5, max = 25, message = "{template.title.size}")
        String title,

        @NotNull(message = "{template.content.not_null}") @Size(min = 5, max = 225, message = "{template.content.size}")
        String content

) {
}
