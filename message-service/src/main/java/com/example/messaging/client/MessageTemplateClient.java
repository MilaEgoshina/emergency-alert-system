package com.example.messaging.client;

import com.example.messaging.dto.response.MessageTemplateHistoryResponse;
import com.example.messaging.dto.response.MessageTemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Интерфейс позволяет вызывать методы удаленного микросервиса (template), который предоставляет функционал для
 * работы с шаблонами уведомлений.
 */
@FeignClient(name = "${services.template}")
public interface MessageTemplateClient {

    /**
     * Метод используется для получения информации о конкретном шаблоне по идентификатору клиента и шаблона.
     * @param senderId значение заголовка запроса, которое указывает идентификатор клиента.
     * @param templateId переменная пути, которая указывает идентификатор шаблона.
     * @return ResponseEntity<MessageTemplateResponse> - ответ на запрос, содержащий объект MessageTemplateResponse.
     */
    @GetMapping("/api/v1/templates/{id}")
    ResponseEntity<MessageTemplateResponse> receiveTemplateBySenderIdAndTemplateId(
            @RequestHeader Long senderId,
            @PathVariable("id") Long templateId
    );

    /**
     * Метод используется для создания исторической записи о шаблоне для конкретного клиента и шаблона.
     * @param senderId значение заголовка запроса, которое указывает идентификатор отправителя.
     * @param templateId переменная пути, которая указывает идентификатор шаблона.
     * @return ResponseEntity<MessageTemplateHistoryResponse> - ответ на запрос, содержащий объект MessageTemplateHistoryResponse.
     */
    @PostMapping("/api/v1/templates/history/{id}")
    ResponseEntity<MessageTemplateHistoryResponse> createMessageTemplateHistory(
            @RequestHeader Long senderId,
            @PathVariable("id") Long templateId
    );

    /**
     * Метод используется для получения исторических данных о конкретном шаблоне для определенного клиента.
     * @param senderId значение заголовка запроса, которое указывает идентификатор клиента.
     * @param templateId переменная пути, которая указывает идентификатор шаблона.
     * @return ResponseEntity<MessageTemplateHistoryResponse> - ответ на запрос, содержащий объект MessageTemplateHistoryResponse.
     */
    @GetMapping("/api/v1/templates/history/{id}")
    ResponseEntity<MessageTemplateHistoryResponse> receiveMessageTemplateHistory(
            @RequestHeader Long senderId,
            @PathVariable("id") Long templateId
    );
}
