package com.example.recipient.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;

/**
 * Класс для представления запросов на получение географической информации.
 */
@Builder
public class LocationDataRequest {

    @DecimalMin(value = "-90.0", message = "Широта должна быть в диапазоне [-90, 90]")
    @DecimalMax(value = "90.0", message = "Широта должна быть в диапазоне [-90, 90]")
    private double latitude;

    @DecimalMin(value = "-180.0", message = "Долгота должна быть в диапазоне [-180, 180]")
    @DecimalMax(value = "180.0", message = "Долгота должна быть в диапазоне [-180, 180]")
    private double longitude;


}
