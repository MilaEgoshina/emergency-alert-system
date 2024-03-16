package com.example.messaging.dto.response;

import java.util.Map;

/**
 * Класс - record представляет собой объект передачи данных для ответов, содержащих информацию о ссылке и связанных с ней опциях.
 */
public record LinkResponse(

        Long linkId, // уникальный идентификатор ссылки.
        Map<String, String> linkOptions //  словарь (отображение), где ключом является имя опции,
        // а значением - значение этой опции для данной ссылки.
) {
}
