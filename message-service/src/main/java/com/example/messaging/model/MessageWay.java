package com.example.messaging.model;

import lombok.AllArgsConstructor;

/**
 * Перечисление способов отправки сообщения.
 */
@AllArgsConstructor
public enum MessageWay implements CodedEntity{

    EMAIL("EML"),
    PHONENUMBER("PHN"),
    TELEGRAM("TGM");

    private final String identifier;

    /**
     * Метод для получения уникального идентификатора способа отправки сообщения.
     *
     * @return уникальный идентификатор способа отправки сообщения
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }
}
