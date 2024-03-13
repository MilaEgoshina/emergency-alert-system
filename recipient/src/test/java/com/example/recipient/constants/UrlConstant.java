package com.example.recipient.constants;

import lombok.Getter;


/**
 * Класс - перечисление, который используется для хранения и централизованного управления URL-путями (эндпойнтами).
 */
@Getter
public enum UrlConstant {

    RECIPIENTS_ENDPOINT("/api/v1/recipients/");

    // хранение значения URL-пути
    private final String value;

    UrlConstant(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
