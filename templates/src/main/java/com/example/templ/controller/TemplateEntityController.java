package com.example.templ.controller;

import com.example.templ.dto.request.TemplateEntityRequest;
import com.example.templ.dto.response.TemplateEntityResponse;
import com.example.templ.service.TemplateEntityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Контроллер для управления сущностями шаблонов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateEntityController {

    private final TemplateEntityService templateEntityService;

    /**
     * Создает новый шаблон.
     *
     * @param clientId идентификатор клиента
     * @param request запрос на создание шаблона
     * @return ответ с созданным шаблоном
     */
    @PostMapping("/")
    @Operation(summary = "Создать шаблон")
    public ResponseEntity<TemplateEntityResponse> createTemplate(
            @RequestHeader Long clientId,
            @RequestBody @Valid TemplateEntityRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateEntityService.createTemplate(clientId, request));
    }

    /**
     * Получает шаблон по его идентификатору.
     *
     * @param clientId   идентификатор клиента
     * @param templateId идентификатор шаблона
     * @return ответ с запрошенным шаблоном
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить шаблон по ID")
    public ResponseEntity<TemplateEntityResponse> getTemplate(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(OK).body(templateEntityService.getTemplate(clientId, templateId));
    }

    /**
     * Удаляет шаблон по его идентификатору.
     *
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @return ответ с результатом удаления (успешно или нет)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить шаблон по ID")
    public ResponseEntity<Boolean> deleteTemplate(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(OK).body(templateEntityService.deleteTemplate(clientId, templateId));
    }

}
