package com.example.sender.dto.response;

import lombok.Getter;

/**
 * Класс, который представляет информацию о шаблоне уведомления
 */
@Getter
public class TemplateHistoryResponse {
    private final Long id; // идентификатор шаблона
    private final String title; // заголовок шаблона
    private final String content; // содержимое шаблона
    private final String imageUrl; // URL изображения для шаблона

    private TemplateHistoryResponse(Long id, String title, String content, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public static TemplateHistoryResponseBuilder builder() {
        return new TemplateHistoryResponseBuilder();
    }


     static class TemplateHistoryResponseBuilder {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;

        public TemplateHistoryResponseBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public TemplateHistoryResponseBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public TemplateHistoryResponseBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public TemplateHistoryResponseBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public TemplateHistoryResponse build() {
            return new TemplateHistoryResponse(id, title, content, imageUrl);
        }
    }
}
