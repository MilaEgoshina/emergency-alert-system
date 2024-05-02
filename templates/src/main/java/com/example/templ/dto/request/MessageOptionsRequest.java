package com.example.templ.dto.request;

import java.util.List;

/**
 * Запрос на получение вариантов сообщений.
 */
public record MessageOptionsRequest(

        /**
         * Список вариантов сообщений.
         */
        List<String> messageOptions
) {
}
