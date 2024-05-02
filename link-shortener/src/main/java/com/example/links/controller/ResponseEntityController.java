package com.example.links.controller;

import com.example.links.dto.request.MessageOptionsRequest;
import com.example.links.dto.response.LinkEntityResponse;
import com.example.links.service.ResponseEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Класс представляет собой контроллер для обработки HTTP запросов связанных с ответами.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responses")
public class ResponseEntityController {

    private final ResponseEntityService responseEntityService;

    /**
     * Метод для создания нового ответа с заданными параметрами.
     * @param optionsRequest объект с настройками сообщения
     * @return ResponseEntity с идентификатором созданного ответа
     */
    @PostMapping("/")
    public ResponseEntity<Long> create(
            @RequestBody @Valid MessageOptionsRequest optionsRequest
    ) {
        return ResponseEntity.status(CREATED).body(responseEntityService.createResponseEntity(optionsRequest));
    }

    /**
     * Метод для генерации ссылки на ответ по его идентификатору.
     * @param responseId идентификатор ответа
     * @return ResponseEntity с сгенерированной ссылкой на ответ
     */
    @PostMapping("/generate/{id}")
    public ResponseEntity<LinkEntityResponse> generate(
            @PathVariable("id") Long responseId
    ) {
        return ResponseEntity.status(CREATED).body(responseEntityService.generateResponse(responseId));
    }
}
