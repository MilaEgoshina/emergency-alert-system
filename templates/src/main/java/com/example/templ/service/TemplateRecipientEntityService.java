package com.example.templ.service;

import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.request.RecipientListRequest;
import com.example.templ.dto.response.RecipientEntityResponse;
import com.example.templ.dto.response.TemplateEntityResponse;
import com.example.templ.entity.TemplateEntity;
import com.example.templ.exception.templatexcpetions.TemplateEntityNotFoundException;
import com.example.templ.mapper.TemplateEntityMapper;
import com.example.templ.repository.RecipientIdRepository;
import com.example.templ.repository.TemplateEntityRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Класс-сервис для управления получателями в шаблонах.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateRecipientEntityService {


    private final TemplateEntityRepository templateEntityRepository;

    private final RecipientIdRepository recipientIdRepository;

    private final RecipientEntityClient recipientEntityClient;

    private final MessageService messageService;

    private final TemplateEntityMapper templateEntityMapper;


    /**
     * Метод для добавления получателей к шаблону.
     *
     * @param clientId Идентификатор клиента.
     * @param templateId Идентификатор шаблона.
     * @param listRequest Запрос на добавление получателей.
     * @return Ответ на запрос с добавленными получателями к шаблону.
     */
    public TemplateEntityResponse addRecipients(Long clientId, Long templateId, RecipientListRequest listRequest) {
        TemplateEntity template = templateEntityRepository.getByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateEntityNotFoundException(
                        messageService.getMessage("template.not_found_error", templateId, clientId)
                ));

        for (Long recipientId : listRequest.recipientIds()) {
            if (recipientIdRepository.checkIfRecipientExistsForTemplateIdAndRecipientId(templateId, recipientId)) {
                log.warn("Такой получатель {} уже есть в шаблоне {}", recipientId, templateId);
                continue;
            }

            try {
                Optional.of(recipientEntityClient.getRecipientById(clientId, recipientId))
                        .map(ResponseEntity::getBody)
                        .ifPresent(recipientResponse -> template.addRecipient(recipientResponse.id()));
                templateEntityRepository.save(template);
            } catch (FeignException.NotFound e) {
                log.warn("Получатель {} не найден для этого клиента {}", recipientId, clientId);
            }
        }

        return templateEntityMapper.toResponse(template, recipientEntityClient);
    }

    /**
     * Метод для удаления получателей из шаблона.
     *
     * @param clientId Идентификатор клиента.
     * @param templateId Идентификатор шаблона.
     * @param request Запрос на удаление получателей.
     * @return Ответ на запрос с удаленными получателями из шаблона.
     */
    public TemplateEntityResponse removeRecipients(Long clientId, Long templateId, RecipientListRequest request) {
        TemplateEntity template = templateEntityRepository.getByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateEntityNotFoundException(
                        messageService.getMessage("template.not_found_error", templateId, clientId)
                ));

        for (Long recipientId : request.recipientIds()) {
            if (templateEntityRepository.checkIfTemplateExistsForRecipientAndTemplate(templateId, recipientId)) {
                Optional.of(recipientEntityClient.getRecipientById(clientId, recipientId))
                        .map(ResponseEntity::getBody)
                        .map(RecipientEntityResponse::id)
                        .ifPresent(template::removeRecipient);
            } else {
                log.warn("Получатель {} не найден в этом шаблоне {}", recipientId, templateId);
            }
        }

        templateEntityRepository.save(template);
        return templateEntityMapper.toResponse(template, recipientEntityClient);
    }

}
