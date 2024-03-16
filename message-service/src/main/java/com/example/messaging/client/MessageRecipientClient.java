package com.example.messaging.client;

import com.example.messaging.dto.response.MessageRecipientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Интерфейс, который позволяет взаимодействовать с сервисом получателей уведомлений и получать информацию о
 * получателях по их идентификаторам.
 */
@FeignClient(name = "${services.recipient}")
public interface MessageRecipientClient {

    /**
     * Метод представляет GET запрос по адресу "/api/v1/recipients/{id}", где {id} - это идентификатор получателя,
     * который передается в качестве параметра метода.
     *
     * @param senderId заголовок запроса, который содержит идентификатор отправителя.
     * @param recipientId идентификатор получателя, который будет подставлен в URL запроса.
     * @return возвращает объект ResponseEntity, который является ответом на запрос и содержит тело ответа, заголовки
     * и статус ответа.
     */
    @GetMapping("/api/v1/recipients/{id}")
    ResponseEntity<MessageRecipientResponse> getBySenderIdAndRecipientId(
            @RequestHeader Long senderId,
            @PathVariable("id") Long recipientId
    );
}
