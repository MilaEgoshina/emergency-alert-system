package com.example.sender.model;

public enum NotificationState implements CodedEnumEntity{

    FRESH("N"), // Newly created
    IN_PROGRESS("P"), // Currently being sent
    DELIVERED("S"), // Successfully sent
    QUEUED_RETRY("R"), // Queued for resending
    FAILED("E"), // Errors occurred during sending
    INVALID("C"); // Cannot be sent (e.g., invalid recipient)

    private final String code;

    NotificationState(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return null;
    }
}
