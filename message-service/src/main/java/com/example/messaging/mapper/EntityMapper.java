package com.example.messaging.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Интерфейс для автоматического создания кода сопоставления, который будет выполнять преобразования между
 * объектами E, R, D.
 */
public interface EntityMapper<E, R, D> {

    /**
     * Метод преобразует объект R в объект E.
     */
    @Mapping(target = "id", ignore = true)
    E toEntity(R request);

    /**
     * Метод преобразует объект E в объект D.
     */
    D toDTO(E entity);

    /**
     * Обновляет объект E на основе объекта R
     */
    @Mapping(target = "id", ignore = true)
    E update(R request, @MappingTarget E entity);
}