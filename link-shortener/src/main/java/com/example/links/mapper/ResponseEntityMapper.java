package com.example.links.mapper;


import com.example.links.dto.request.MessageOptionsRequest;
import com.example.links.entity.ResponseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseEntityMapper {

    ResponseEntity toResponse(MessageOptionsRequest optionsRequest);
}
