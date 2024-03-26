package com.example.templ.builder;

public record TemplateEntityJson(

        String title,
        String content
) implements TemplateEntityTest{

    private static final String TEMPLATE = """
            {
                "title": %s,
                "content": %s
            }
            """;

    @Override
    public String convertToJson() {
        String titleVal = format(title);
        String contentVal = format(content);
        return TEMPLATE.formatted(titleVal, contentVal);
    }
}
