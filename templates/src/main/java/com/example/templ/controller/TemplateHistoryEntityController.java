package com.example.templ.controller;

import com.example.templ.dto.response.TemplateHistoryEntityResponse;
import com.example.templ.service.TemplateHistoryEntityService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления историей шаблонов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates/history")
public class TemplateHistoryEntityController {

    private final TemplateHistoryEntityService templateHistoryEntityService;

    /**
     * Создает новую запись в истории шаблона на основе существующего шаблона.
     *
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @return ответ с созданной записью в истории шаблона
     */
    @PostMapping("/{id}")
    @Operation(summary = "Создать новую запись в историю шаблона на основе существующего шаблона")
    public ResponseEntity<TemplateHistoryEntityResponse> createTemplateHistory(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateHistoryEntityService
                .createTemplateHistory(clientId, templateId));
    }

    /**
     * Получает историю шаблона по его идентификатору.
     *
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @return ответ с историей запрошенного шаблона
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить историю шаблона по ID")
    public ResponseEntity<TemplateHistoryEntityResponse> getTemplateHistory(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(templateHistoryEntityService
                .getTemplateHistory(clientId, templateId));
    }
}
