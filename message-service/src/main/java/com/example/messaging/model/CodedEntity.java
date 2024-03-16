package com.example.messaging.model;


/**
 * Интерфейс, который служит контрактом для объектов, имеющих некоторый уникальный код или идентификатор,
 * представленный в виде строки.
 */
public interface CodedEntity {

    String getIdentifier();
}
