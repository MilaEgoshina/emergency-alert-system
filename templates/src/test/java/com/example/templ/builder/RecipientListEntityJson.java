package com.example.templ.builder;

import org.springframework.util.StringUtils;

import java.util.List;

public record RecipientListEntityJson(

        List<Integer> recipientIds
) {

    private static final String TEMPLATE = """
            {
                 "recipientIds": [
                     %s
                 ]
             }
            """;

    public String convertToJson() {
        return TEMPLATE.formatted(StringUtils.collectionToDelimitedString(recipientIds, ","));
    }
}
