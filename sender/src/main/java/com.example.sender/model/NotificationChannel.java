package com.example.sender.model;

public enum NotificationChannel implements CodedEnumEntity{

    EMAIL("EML"),
    PHONE("PHN"),
    TELEGRAM("TGM");

    private final String code;

    NotificationChannel(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return null;
    }
}
