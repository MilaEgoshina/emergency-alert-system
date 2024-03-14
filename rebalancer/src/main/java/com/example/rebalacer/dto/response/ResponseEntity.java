package com.example.rebalacer.dto.response;

import lombok.Getter;

import java.util.Objects;

/**
 * Класс для инкапсуляции данных, которые будут отправлены в ответе на запрос клиента
 */
@Getter
public class ResponseEntity {

    private final Long entityId; // идентификатор ответа.
    private final String entityTitle; // заголовок ответа.
    private final String entityContent; // содержимое ответа.
    private final String imageSource; // URL-адрес изображения, связанного с ответом.

    public ResponseEntity(Long id, String title, String content, String imageUrl) {
        this.entityId = id;
        this.entityTitle = title;
        this.entityContent = content;
        this.imageSource = imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ResponseEntity other = (ResponseEntity) obj;
        return Objects.equals(entityId, other.entityId)
                && Objects.equals(entityTitle, other.entityTitle)
                && Objects.equals(entityContent, other.entityContent)
                && Objects.equals(imageSource, other.imageSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, entityTitle, entityContent, imageSource);
    }
}
