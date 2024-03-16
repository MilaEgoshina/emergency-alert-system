package com.example.messaging.client;

import com.example.messaging.dto.response.LinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Интерфейс, который предоставляет клиентский доступ к функциональности сокращения URL-адресов.
 */
@FeignClient(name = "${services.shortener}") // используется для взаимодействия с другим микросервисом(shortener).
public interface LinkShortenerClient {

    /**
     * Метод представляет собой запрос к другому сервису для сокращения URL-адреса.
     * @param responseId идентификатор ответа.
     * @return возвращает ResponseEntity с объектом UrlsResponse, который содержит информацию о сокращенном URL-адресе.
     */
    @PostMapping("/api/v1/responses/generate/{id}")
    ResponseEntity<LinkResponse> getShorterLink(
            @PathVariable("id") Long responseId
    );
}
