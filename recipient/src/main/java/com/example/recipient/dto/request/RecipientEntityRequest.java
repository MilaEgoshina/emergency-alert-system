package com.example.recipient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


/**
 * Класс для определения структуры данных, которые мы ожидаем получить от пользователя при создании нового получателя.
 */
@Builder
@Getter
public class RecipientEntityRequest {

    @Size(max = 50, message = "{recipient.name.size}")
    private String name;
    @NotNull(message = "{recipient.email.not_null}") @Email(message = "{recipient.email.invalid}") @Size(max = 255, message = "{recipient.email.size}")
    private String email;
    @Size(max = 20, message = "{recipient.phone.size}")
    private String phoneNumber;
    @Size(max = 20, message = "{recipient.telegram.size}")
    private String telegramId;
    @Valid
    private LocationDataRequest locationDataRequest;

    public RecipientEntityRequest(String name, String email, String phoneNumber, String telegramId, LocationDataRequest locationDataRequest) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.telegramId = telegramId;
        this.locationDataRequest = locationDataRequest;
    }

    public RecipientEntityRequest(){}


}
