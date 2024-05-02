package com.example.messaging.controller;

import com.example.messaging.dto.kafka.MessageKafka;
import com.example.messaging.dto.response.MessageHistoryResponse;
import com.example.messaging.dto.response.MessageResponse;
import com.example.messaging.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * Контроллер сообщений для операций по управлению уведомлениями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    /**
     * Метод для отправки уведомления всем получателям, зарегистрированным для указанного шаблона уведомления.
     *
     * @param clientId  идентификатор отправителя сообщения из заголовка запроса.
     * @param templateId идентификатор шаблона сообщения из пути URL.
     * @return возвращает строковое представление результата операции в теле ответа со статусом OK.
     */
    @PostMapping("/{id}")
    @Operation(summary = "Отправить сообщения всем получателям, зарегистрированным для указанного идентификатора шаблона")
    public ResponseEntity<String> notify(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        //отправка сообщения всем получателям, зарегистрированным для данного templateId, используя clientId для идентификации клиента.
        return ResponseEntity.status(OK).body(messageService.distributeMessages(clientId, templateId));
    }

    /**
     * Метод для установки статуса уведомления как успешно отправленного получателю.
     *
     * @param clientId идентификатор клиента из заголовка запроса.
     * @param messageId идентификатор сообщения из пути URL.
     * @return возвращает объект MessageHistoryResponse в теле ответа со статусом OK, который содержит информацию
     * об истории сообщения.
     */
    @PostMapping("/{id}/delivered")
    @Operation(summary = "Установить статус уведомления как успешно отправленное получателю")
    public ResponseEntity<MessageHistoryResponse> markMessageAsDelivered(
            @RequestHeader Long clientId,
            @PathVariable("id") Long messageId
    ) {

        // обновление статуса сообщения с данным messageId как успешно отправленного для клиента с clientId
        return ResponseEntity.status(OK).body(messageService.setMessageAsDelivered(clientId, messageId));
    }


    /**
     * Метод для установки статуса уведомления как отправленного с ошибками.
     *
     * @param clientId идентификатор клиента из заголовка запроса.
     * @param messageId идентификатор сообщения из пути URL.
     * @return возвращает объект MessageHistoryResponse в теле ответа со статусом OK, который содержит информацию
     * об истории сообщения.
     */
    @PostMapping("/{id}/failed")
    @Operation(summary = "Установить статус уведомления как отправленное с ошибками")
    public ResponseEntity<MessageHistoryResponse> markMessageAsFailed(
            @RequestHeader Long clientId,
            @PathVariable("id") Long messageId
    ) {
        // обновление статуса сообщения с данным messageId как ошибочного для клиента с clientId.
        return ResponseEntity.status(OK).body(messageService.setMessageAsFailed(clientId, messageId));
    }

    /**
     * Метод для установки статуса уведомления как невозможного для отправки.
     *
     * @param clientId идентификатор клиента из заголовка запроса.
     * @param messageId идентификатор сообщения из пути URL.
     * @return возвращает объект MessageHistoryResponse в теле ответа со статусом OK, который, содержит информацию
     * об истории сообщения.
     */
    @PostMapping("/{id}/invalid")
    @Operation(summary = "Установить статус уведомления как невозможно отправить")
    public ResponseEntity<MessageHistoryResponse> markMessageAsInvalid(
            @RequestHeader Long clientId,
            @PathVariable("id") Long messageId
    ) {
        return ResponseEntity.status(OK).body(messageService.setMessageAsInvalid(clientId, messageId));
    }

    /**
     * Метод для установки статуса уведомления как ожидающего повторной отправки.
     *
     * @param clientId идентификатор клиента из заголовка запроса.
     * @param messageId идентификатор сообщения из пути URL.
     * @return возвращает объект MessageResponse в теле ответа со статусом OK, который содержит информацию об уведомлении.
     */
    @PostMapping("/{id}/resending")
    @Operation(summary = "Установить статус уведомления как ожидающее повторной отправки")
    public ResponseEntity<MessageResponse> markMessageForResending(
            @RequestHeader Long clientId,
            @PathVariable("id") Long messageId
    ) {
        return ResponseEntity.status(OK).body(messageService.setMessageAsResending(clientId, messageId));
    }

    /**
     * Метод для получения списка сообщений, которые ожидают повторной отправки, находятся в состоянии
     * ожидания или являются новыми, для балансировки нагрузки при отправке уведомлений.
     *
     * @param pendingSec время в секундах для поиска уведомлений в состоянии ожидания.
     * @param newSec время в секундах для поиска новых уведомлений.
     * @param size максимальное количество уведомлений для возврата.
     * @return возвращает список объектов MessageKafka в теле ответа со статусом OK, который содержит информацию об
     * уведомлениях для балансировки нагрузки.
     */
    @GetMapping("/")
    @Operation(summary = "ДЛЯ REBALANCER: получить уведомления, ожидающие повторной отправки/в состоянии ожидания/новые (установить состояние ожидания)")
    public ResponseEntity<List<MessageKafka>> receiveMessagesForRebalancing(
            @RequestParam(name = "pending", required = false, defaultValue = "10") Long pendingSec,
            @RequestParam(name = "new", required = false, defaultValue = "10") Long newSec,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
    ) {

        //возвращает список сообщений, соответствующих указанным критериям времени и количеству.
        return ResponseEntity.status(OK).body(
                messageService.receiveMessagesForRebalancing(pendingSec, newSec, size)
        );
    }

}
