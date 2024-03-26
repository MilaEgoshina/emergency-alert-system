package com.example.templ.client;

import com.example.templ.dto.request.MessageOptionsRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Интерфейс для взаимодействия с удаленным сервисом, который предоставляет функциональность сокращения ссылок.
 */
@FeignClient(name = "${services.link-shortener}") // имя удаленного сервиса, с которым этот клиент будет взаимодействовать.
public interface LinkShortenerClient {

    /**
     * Метод для выполнения HTTP POST запроса на указанный путь `/api/v1/responses/`.
     *
     * @param messageRequest объект MessageOptionsRequest в качестве тела запроса.
     * @return возвращает объект типа ResponseEntity<Long>, обертку для HTTP-ответа.
     */
    @PostMapping("/api/v1/responses/")
    ResponseEntity<Long> generate(
            @RequestBody @Valid MessageOptionsRequest messageRequest
    );

}
