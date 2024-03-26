package com.example.links.dto.response;

import java.util.Map;

public record LinkEntityResponse(

        Long linkId,
        Map<String, String> linkOptionMap

) {
}
