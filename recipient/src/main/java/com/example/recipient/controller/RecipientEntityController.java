package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientEntityRequest;
import com.example.recipient.dto.response.RecipientEntityResponse;
import com.example.recipient.service.RecipientEntityService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Класс - контроллер, который обрабатывает HTTP-запросы, связанные с сущностями получателей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipients")
public class RecipientEntityController {

    private final RecipientEntityService recipientEntityService;

    /**
     * Метод для регистрации получателя
     * @param clientId заголовок Long clientId
     * @param entityRequest объект RecipientEntityRequest в теле запроса
     * @return возвращает код состояния HTTP 201 (Created) и ответ в виде объекта RecipientEntityResponse.
     */
    @PostMapping("/")
    @Operation(summary = "Регистрация получателя")
    public ResponseEntity<RecipientEntityResponse> register(
            @RequestHeader Long clientId,
            @RequestBody @Valid RecipientEntityRequest entityRequest
    ) {

        return new ResponseEntity<>(recipientEntityService.register(clientId, entityRequest), HttpStatus.CREATED);
    }

    /**
     * Метод для получения всех получателей
     * @param clientId заголовок Long clientId
     * @return возвращает код состояния HTTP 200 (OK) и ответ в виде списка объектов RecipientEntityResponse.
     */
    @GetMapping("/")
    @Operation(summary = "Получение всех получателей по ID клиента")
    public ResponseEntity<List<RecipientEntityResponse>> receiveByClientId(
            @RequestHeader Long clientId
    ) {
        return new ResponseEntity<>(recipientEntityService.receiveByClient(clientId), HttpStatus.OK);
    }

    /**
     * Метод для получения получателя по ID
     * @param clientId заголовок Long clientId
     * @param recipientId параметр пути Long recipientId.
     * @return возвращает код состояния HTTP 200 (OK) и ответ в виде объекта RecipientEntityResponse.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение получателя по ID")
    public ResponseEntity<RecipientEntityResponse> receive(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    ) {
        return new ResponseEntity<>(recipientEntityService.receive(clientId, recipientId), HttpStatus.OK);
    }

    /**
     * Метод для удаления получателя по ID
     * @param clientId заголовок Long clientId
     * @param recipientId параметр пути Long recipientId.
     * @return возвращает код состояния HTTP 200 (OK) и ответ в виде логического значения (true или false).
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление получателя по ID")
    public ResponseEntity<Boolean> delete(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    ) {
        return new ResponseEntity<>(recipientEntityService.delete(clientId, recipientId), HttpStatus.OK);
    }

    /**
     * Метод для обновления получателя по ID
     * @param clientId заголовок Long clientId
     * @param recipientId параметр пути Long recipientId.
     * @param entityRequest объект RecipientEntityRequest в теле запроса.
     * @return возвращает код состояния HTTP 200 (OK) и ответ в виде объекта RecipientEntityResponse.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление получателя по ID")
    public ResponseEntity<RecipientEntityResponse> update(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId,
            @RequestBody @Valid RecipientEntityRequest entityRequest
    ) {
        return new ResponseEntity<>(recipientEntityService.update(clientId, recipientId, entityRequest), HttpStatus.OK);
    }

    /**
     * Метод для получения всех получателей по ID шаблона
     * @param clientId заголовок Long clientId
     * @param templateId параметр пути Long templateId.
     * @return возвращает код состояния HTTP 200 (OK) и ответ в виде списка объектов RecipientEntityResponse.
     */
    @GetMapping("/template/{id}")
    @Operation(summary = "Получение всех получателей по ID шаблона")
    public ResponseEntity<List<RecipientEntityResponse>> receiveByTemplateId(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return new ResponseEntity<>(recipientEntityService.receiveByTemplateEntity(clientId, templateId), HttpStatus.OK);
    }
}


