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

    // метод преобразует объект TemplateEntity в объект TemplateHistoryEntity, который представляет историю изменений шаблона.
    TemplateHistoryEntity toTemplateHistory(TemplateEntity templateEntity);

    // метод преобразует объект TemplateHistoryEntity в объект TemplateHistoryEntityResponse, который используется для передачи
    // данных истории изменений шаблона на клиентскую сторону.
    TemplateHistoryEntityResponse toTemplateHistoryResponse(TemplateHistoryEntity historyEntity);

    /**
     * Метод для преобразования объекта класса TemplateEntity в объект класса TemplateEntityResponse, добавляя поле recipientIds
     * с помощью информации, полученной с помощью клиента RecipientEntityClient.
     *
     * @param templateEntity объект класса TemplateEntity - шаблона сообщения.
     * @param recipientClient объект интерфейса RecipientClient для преобразования объектов.
     * @return преобразованный объект класса TemplateEntityResponse.
     */
    @Mapping( // определяем маппинг между полями объектов TemplateEntityResponse и TemplateEntity
            target = "recipientIds", // поле recipientIds в TemplateEntityResponse

            // содержит выражение, которое будет выполнено для получения значения поля recipientIds.
            expression = "java(recipientClient.getRecipientResponseListByTemplateId(templateEntity.getClientId(), templateEntity.getId()).getBody())"

    )
    TemplateEntityResponse toResponse(TemplateEntity templateEntity, @Context RecipientEntityClient recipientClient);

}
