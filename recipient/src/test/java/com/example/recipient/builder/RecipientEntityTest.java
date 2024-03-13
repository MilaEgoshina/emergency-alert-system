package com.example.recipient.builder;

public record RecipientEntityTest(

        String email,

        String phoneNumber,

        String telegramId) {

    private static final String TEMPLATE = """
            {
                "name": "Jimmy",
                "email": %s,
                "phoneNumber": %s,
                "telegramId": %s,
                "geolocation": {
                    "latitude": 90.0000,
                    "longitude": -90.0000
                }
            }
            """;


    public String toJson() {
        String recipientEmail = getFormat(email);
        String recipientPhone = getFormat(phoneNumber);
        String recipientTelegram = getFormat(telegramId);
        return String.format(TEMPLATE, recipientEmail, recipientPhone, recipientTelegram);
    }

    private String getFormat(String string) {
        return string == null ? "null" : "\"" + string + "\"";
    }
}
