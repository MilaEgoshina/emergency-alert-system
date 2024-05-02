package com.example.templ.dto.response;

import java.util.List;


/**
 * Класс TemplateEntityResponse представляет собой объект ответа, содержащий информацию о шаблоне сообщения.
 */
public record TemplateEntityResponse(

        /**
         * Уникальный идентификатор шаблона
         */
        Long id,

        /**
         * Заголовок шаблона
         */
        String templateTitle,

        /**
         * Содержимое шаблона
         */
        String templateContent,

        /**
         * Изображение, связанное с шаблоном
         */
        String templateImage,

        /**
         * Список идентификаторов получателей шаблона
         */
        List<RecipientEntityResponse> recipientIds
) {
}
