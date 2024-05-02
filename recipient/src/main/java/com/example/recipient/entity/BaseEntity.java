package com.example.recipient.entity;

import java.io.Serializable;

/**
 * Интерфейс базовой сущности.
 * Определяет методы для получения и установки идентификатора сущности.
 *
 * @param <T> Тип идентификатора сущности.
 */
public interface BaseEntity<T extends Serializable> {

    /**
     * Получает идентификатор сущности.
     *
     * @return Идентификатор сущности.
     */
    T getId();

    /**
     * Устанавливает идентификатор сущности.
     *
     * @param id Новый идентификатор сущности.
     */
    void setId(T id);
}
