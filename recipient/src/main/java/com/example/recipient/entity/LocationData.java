package com.example.recipient.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
//Класс LocationData встраивается в сущность Recipient как составная часть
public class LocationData {

    private double latitude;
    private double longitude;
}
