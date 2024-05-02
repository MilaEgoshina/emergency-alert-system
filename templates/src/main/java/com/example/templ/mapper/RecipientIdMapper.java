package com.example.templ.mapper;


import com.example.templ.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.templ.entity.RecipientId;
import com.example.templ.entity.TemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс-маппер для преобразования объектов типа RecipientTemplateKafkaRecord в объекты типа RecipientId.
 */
@Mapper(
        componentModel = "spring",
        imports = {
                TemplateEntity.class
        }
)
public interface RecipientIdMapper {

    /**
     * Метод преобразует объект типа RecipientTemplateKafkaRecord в объект типа RecipientId, устанавливая при этом
     * значение свойства templateEntity на основе данных из templateKafka.
     *
     * @param templateKafka Объект типа RecipientTemplateKafkaRecord, который требуется преобразовать.
     * @return Объект типа RecipientId с установленным значением свойства templateEntity на основе данных из templateKafka.
     */
    @Mapping(target = "templateEntity", // маппинг свойства templateEntity объекта RecipientId

            //создание нового объекта TemplateEntity с помощью паттерна Builder, устанавливая значение ID объекта из
            // templateKafka и возвращение этого объекта.
            expression = "java(TemplateEntity.builder().id(templateKafka.templateId()).build())")
    RecipientId toEntity(RecipientTemplateKafkaRecord templateKafka);
}
