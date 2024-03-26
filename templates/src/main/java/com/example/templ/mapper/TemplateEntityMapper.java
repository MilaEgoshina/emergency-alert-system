package com.example.templ.mapper;

import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.request.TemplateEntityRequest;
import com.example.templ.dto.response.TemplateEntityResponse;
import com.example.templ.dto.response.TemplateHistoryEntityResponse;
import com.example.templ.entity.TemplateEntity;
import com.example.templ.entity.TemplateHistoryEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemplateEntityMapper extends EntityMapper<TemplateEntity, TemplateEntityRequest, TemplateEntityResponse>{

    TemplateHistoryEntity toTemplateHistory(TemplateEntity templateEntity);

    TemplateHistoryEntityResponse toTemplateHistoryResponse(TemplateHistoryEntity historyEntity);

    @Mapping(
            target = "recipientIds",
            expression = "java(recipientClient.getRecipientResponseListByTemplateId(templateEntity.getClientId(), template.getId()).getBody())"

    )
    TemplateEntityResponse toResponse(TemplateEntity templateEntity, @Context RecipientEntityClient recipientClient);

}
