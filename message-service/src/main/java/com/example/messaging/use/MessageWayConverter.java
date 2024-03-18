package com.example.messaging.use;

import com.example.messaging.model.MessageWay;
import jakarta.persistence.Converter;

/**
 * Класс используется для конвертации типа данных MessageWay при сохранении и извлечении его из базы данных.
 */
@Converter(autoApply = true)
public class MessageWayConverter extends EntityCodeConverter<MessageWay>{


    public MessageWayConverter(){

        super(MessageWay.class);
    }
}
