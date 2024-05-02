package com.example.templ.controller;

import com.example.templ.dto.request.RecipientListRequest;
import com.example.templ.dto.response.TemplateEntityResponse;
import com.example.templ.service.TemplateRecipientEntityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Контроллер для управления получателями шаблонов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateRecipientEntityController {

    private final TemplateRecipientEntityService templateRecipientEntityService;


    /**
     * Добавляет получателей к шаблону.
     *
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @param listRequest запрос на добавление списка получателей
     * @return ответ с обновленным шаблоном
     */
    @PostMapping("/{id}/recipients")
    @Operation(summary = "Добавить получателя к шаблону")
    public ResponseEntity<TemplateEntityResponse> addRecipients(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid RecipientListRequest listRequest
    ) {
        return ResponseEntity.status(CREATED).body(templateRecipientEntityService.addRecipients(clientId, templateId, listRequest));
    }

    /**
     * Удаляет получателей из шаблона.
     *
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @param listRequest запрос на удаление списка получателей
     * @return ответ с обновленным шаблоном
     */
    @DeleteMapping("/{id}/recipients")
    @Operation(summary = "Удалить получателя из шаблона")
    public ResponseEntity<TemplateEntityResponse> removeRecipients(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid RecipientListRequest listRequest
    ) {
        return ResponseEntity.status(OK).body(templateRecipientEntityService.removeRecipients(clientId, templateId, listRequest));
    }

}
