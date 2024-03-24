package com.example.security.builder;

public interface SecurityEntityTest {

    String convertToJson();

    default String format(String string) {
        return string == null ? "null" : "\"" + string + "\"";
    }
}
