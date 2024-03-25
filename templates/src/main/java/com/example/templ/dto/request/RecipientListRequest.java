package com.example.templ.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

/**
 * Класс для создания объекта, который представляет запрос на получение списка получателей.
 */
@Builder
public record RecipientListRequest(

        // Список идентификаторов получателей, который должен быть передан в запросе.
        // Список `recipientIds` не должен быть пустым.
        @Valid @NotEmpty(message = "{recipientList.recipients.min_size}") List<Long> recipientIds
) {
}