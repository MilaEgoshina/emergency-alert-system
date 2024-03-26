package com.example.templ.mapper;


import com.example.templ.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.templ.entity.RecipientId;
import com.example.templ.entity.TemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {
                TemplateEntity.class
        }
)
public interface RecipientIdMapper {

    // метод преобразует объект RecipientTemplateKafkaRecord в объект RecipientId, устанавливая при этом
    // значение свойства templateEntity на основе данных из templateKafka.
    @Mapping(target = "templateEntity", // маппинг свойства templateEntity объекта RecipientId

            //создание нового объекта TemplateEntity с помощью паттерна Builder, устанавливая значение ID объекта из
            // templateKafka и возвращение этого объекта.
            expression = "java(TemplateEntity.builder().id(templateKafka.templateId()).build())")
    RecipientId toEntity(RecipientTemplateKafkaRecord templateKafka);
}
