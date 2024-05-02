package com.example.rebalancer.model;

/**
 * Перечисление способов уведомления.
 * Каждый элемент перечисления представляет собой способ уведомления пользователя.
 * Возможные значения: EMAIL, PHONENUMBER, TELEGRAM.
 */
public enum NotificationWay {

    EMAIL("EML"),
    PHONENUMBER("PHN"),
    TELEGRAM("TGM");

    NotificationWay(String code) {

    }
}
