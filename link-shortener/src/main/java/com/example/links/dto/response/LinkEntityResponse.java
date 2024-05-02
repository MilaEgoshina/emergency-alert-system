package com.example.links.dto.response;

import java.util.Map;

/**
 * Запись LinkEntityResponse представляет объект ответа с данными о возможных опциях ссылки.
 */
public record LinkEntityResponse(

        Long linkId,
        Map<String, String> linkOptionMap

) {
}
