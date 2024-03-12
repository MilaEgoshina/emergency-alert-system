package com.example.recipient.mapper;

import com.example.recipient.dto.request.RecipientEntityRequest;
import com.example.recipient.dto.response.RecipientEntityResponse;
import com.example.recipient.entity.RecipientEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RecipientEntityMapper extends EntityMapper<RecipientEntity, RecipientEntityRequest, RecipientEntityResponse>{
}
