package com.example.messaging.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageWay implements CodedEntity{

    EMAIL("EML"),
    PHONENUMBER("PHN"),
    TELEGRAM("TGM");

    private final String identifier;

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
