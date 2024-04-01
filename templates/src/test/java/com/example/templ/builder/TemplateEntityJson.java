package com.example.templ.builder;

/**
 * Класс для создания JSON-объектов, содержащих данные для написания нового шаблона.
 */
public record TemplateEntityJson(

        String templateTitle,
        String templateContent
) implements TemplateEntityTest{

    private static final String TEMPLATE = """
            {
                "templateTitle": %s,
                "templateContent": %s
            }
            """;

    @Override
    public String convertToJson() {
        String titleVal = format(templateTitle);
        String contentVal = format(templateContent);
        return TEMPLATE.formatted(titleVal, contentVal);
    }
}
