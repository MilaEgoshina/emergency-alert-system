package com.example.templ.dto.response;

import java.util.List;


/**
 * Класс TemplateEntityResponse представляет собой объект ответа, содержащий информацию о шаблоне сообщения.
 *
 * Поля:
 * - id: уникальный идентификатор шаблона
 * - templateTitle: заголовок шаблона
 * - templateContent: содержимое шаблона
 * - templateImage: изображение, связанное с шаблоном
 * - recipientIds: список идентификаторов получателей шаблона
 */
public record TemplateEntityResponse(

        /**
         * Уникальный идентификатор шаблона
         */
        Long id,

        /**
         * заголовок шаблона
         */
        String templateTitle,

        /**
         * содержимое шаблона
         */
        String templateContent,

        /**
         * изображение, связанное с шаблоном
         */
        String templateImage,

        /**
         * список идентификаторов получателей шаблона
         */
        List<RecipientEntityResponse> recipientIds
) {
}
