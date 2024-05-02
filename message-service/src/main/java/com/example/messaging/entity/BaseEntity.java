package com.example.messaging.entity;

import java.io.Serializable;

/**
 * Интерфейс BaseEntity представляет собой основу для всех сущностей в системе.
 * Он определяет базовые методы для работы с идентификатором сущности.
 */
public interface BaseEntity <T extends Serializable>{

    /**
     * Метод, возвращающий идентификатор сущности.
     * @return идентификатор сущности
     */
    T getId();

    /**
     * Метод для установки идентификатора сущности.
     * @param id новый идентификатор сущности
     */
    void setId(T id);
}
