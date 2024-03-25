package com.example.templ.client;

import com.example.templ.dto.response.RecipientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * Интерфейс для взаимодействия с сервисом, предоставляющим информацию о получателях, через Feign клиент,
 * упрощая процесс обмена данными между микросервисами и обеспечивая стандартизацию запросов и ответов.
 */
@FeignClient(name = "${services.recipient}")
public interface RecipientEntityClient {

    /**
     *
     * @param clientId
     * @param recipientId
     * @return
     */
    @GetMapping(value = "/api/v1/recipients/{id}")
    ResponseEntity<RecipientResponse> receiveRecipientById(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    );

    /**
     *
     * @param clientId
     * @param templateId
     * @return
     */
    @GetMapping(value = "/api/v1/recipients/template/{id}")
    ResponseEntity<List<RecipientResponse>> receiveRecipientResponseListByTemplateId(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    );
}
