package com.example.links.mapper;


import com.example.links.dto.request.MessageOptionsRequest;
import com.example.links.entity.ResponseEntity;
import org.mapstruct.Mapper;

/**
 * Интерфейс, который представляет компонент для сопоставления данных между объектами класса MessageOptionsRequest и ResponseEntity.
 */
@Mapper(componentModel = "spring")
public interface ResponseEntityMapper {

    /**
     * Метод преобразования объекта MessageOptionsRequest в объект ResponseEntity.
     * @param optionsRequest объект с параметрами сообщения
     * @return объект ResponseEntity, созданный на основе параметров запроса
     */
    ResponseEntity toResponse(MessageOptionsRequest optionsRequest);
}
