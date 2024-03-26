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
@FeignClient(name = "${services.recipient}") // будет вызываться сервис recipient из конфигурации приложеня.
public interface RecipientEntityClient {

    /**
     * Метод для получения информации о конкретном получателе (recipient) по его идентификатору (id).
     *
     * @param clientId идентификатор клиента, который должен быть передан в заголовке HTTP-запроса.
     * @param recipientId идентификатор получателя, который должен быть передан в пути URL-запроса.
     * @return возвращает ResponseEntity<RecipientResponse>, что означает, что метод должен получить от удаленного сервиса
     * ответ в виде объекта RecipientResponse, обернутого в ResponseEntity.
     */
    @GetMapping(value = "/api/v1/recipients/{id}")
    ResponseEntity<RecipientResponse> getRecipientById(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    );

    /**
     * Метод для получения списка получателей, связанных с определенным шаблоном (template), по идентификатору этого шаблона.
     *
     * @param clientId идентификатор клиента, который должен быть передан в заголовке HTTP-запроса.
     * @param templateId идентификатор шаблона, по которому нужно получить список получателей.
     * @return возвращает ResponseEntity<List<RecipientResponse>>, что означает, что метод должен получить от удаленного
     * сервиса ответ в виде списка объектов RecipientResponse, обернутого в ResponseEntity
     */
    @GetMapping(value = "/api/v1/recipients/template/{id}")
    ResponseEntity<List<RecipientResponse>> getRecipientResponseListByTemplateId(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    );
}
