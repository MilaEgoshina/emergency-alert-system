package com.example.recipient.builder;

public class RecipientEntityBuilderTest{

    private String email;
    private String phoneNumber;
    private String telegramId;

    public static RecipientEntityBuilderTest builder() {
        return new RecipientEntityBuilderTest();
    }

    public RecipientEntityBuilderTest email(String email) {
        this.email = email;
        return this;
    }

    public RecipientEntityBuilderTest phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RecipientEntityBuilderTest telegramId(String telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public RecipientEntityTest build() {
        return new RecipientEntityTest(email, phoneNumber, telegramId);
    }
}
