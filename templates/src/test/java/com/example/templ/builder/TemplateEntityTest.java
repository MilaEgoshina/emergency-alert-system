package com.example.templ.builder;

public interface TemplateEntityTest {

    String convertToJson();

    default String format(String string) {
        return string == null ? "null" : "\"" + string + "\"";
    }
}
