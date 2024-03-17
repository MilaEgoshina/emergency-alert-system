package com.example.messaging.use;

import com.example.messaging.model.CodedEntity;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

/**
 * Класс предназначен для преобразования значений перечислений (enum) в строковые представления и обратно при сохранении
 * и извлечении данных из базы данных.
 */
public class EntityCodeConverter<E extends Enum<E> & CodedEntity> implements AttributeConverter<E, String> {

    private final Class<E> entityClass;

    public EntityCodeConverter(Class<E> entityClass) {

        //передается класс перечисления entityClass, который будет использоваться при преобразовании
        this.entityClass = entityClass;
    }

    /**
     * Метод преобразует значение перечисления entity в строковое представление.
     * @param entity значение перечисления из класса - enum
     * @return строковое представление перечисления entity
     */
    @Override
    public String convertToDatabaseColumn(E entity) {
        if (entity == null) {
            return null;
        }
        // Вызывает метод getIdentifier() на объекте entity и возвращает полученную строку.
        // Эта строка будет сохранена в базе данных.
        return entity.getIdentifier();
    }

    /**
     * Метод преобразует строковое представление identifier обратно в значение перечисления.
     * @param identifier строковое представление идентификатора
     * @return значение перечисления из класса - enum.
     */
    @Override
    public E convertToEntityAttribute(String identifier) {
        if (identifier == null) {
            return null;
        }

        return Arrays.stream(entityClass.getEnumConstants()) // получение потока всех констант перечисления entityClass
                .filter(e -> e.getIdentifier().equals(identifier)) // поиск константы перечисления
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
