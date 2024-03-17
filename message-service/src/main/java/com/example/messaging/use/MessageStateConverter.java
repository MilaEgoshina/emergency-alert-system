package com.example.messaging.use;

import com.example.messaging.model.MessageState;
import jakarta.persistence.Converter;

/**
 * Класс используется для конвертации типа данных MessageState при сохранении и извлечении его из базы данных.
 */
@Converter(autoApply = true)
public class MessageStateConverter extends EntityCodeConverter<MessageState>{

    public MessageStateConverter(){
        super(MessageState.class);
    }
}
