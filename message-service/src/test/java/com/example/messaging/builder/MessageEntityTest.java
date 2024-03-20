package com.example.messaging.builder;

public interface MessageEntityTest {

    String toJson();

    default String format(String string) {
        return string == null ? "null" : "\"" + string + "\"";
    }
}
