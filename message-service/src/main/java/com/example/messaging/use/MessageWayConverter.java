package com.example.messaging.use;

import com.example.messaging.model.MessageWay;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MessageWayConverter extends EntityCodeConverter<MessageWay>{


    public MessageWayConverter(){

        super(MessageWay.class);
    }
}
