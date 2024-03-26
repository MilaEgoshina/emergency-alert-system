package com.example.templ.service;

import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.request.TemplateEntityRequest;
import com.example.templ.dto.response.TemplateEntityResponse;
import com.example.templ.exception.templatexcpetions.TemplateEntityCreationException;
import com.example.templ.exception.templatexcpetions.TemplateEntityNotFoundException;
import com.example.templ.exception.templatexcpetions.TemplateTitleAlreadyExistsException;
import com.example.templ.mapper.TemplateEntityMapper;
import com.example.templ.repository.TemplateEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Класс - сервис для обеспечения контроля над созданием, получением и удалением шаблонов в системе.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateEntityService {

    private final TemplateEntityRepository templateEntityRepository;

    private final RecipientEntityClient recipientEntityClient;

    private final MessageService messageService;

    private final TemplateEntityMapper templateEntityMapper;

    public TemplateEntityResponse createTemplate(Long clientId, TemplateEntityRequest request) {
        if (templateEntityRepository.checkIfTemplateExistsForClientAndTitle(clientId, request.title())) {
            throw new TemplateTitleAlreadyExistsException(
                    messageService.getMessage("template.title_already_exists_error",
                            request.title(), clientId)
            );
        }

        return Optional.of(request)
                .map(templateEntityMapper::toEntity)
                .map(template -> template.setClient(clientId))
                .map(templateEntityRepository::save)
                .map(template -> templateEntityMapper.toResponse(template, recipientEntityClient))
                .orElseThrow(() -> new TemplateEntityCreationException(
                        messageService.getMessage("template.creation_error", clientId)
                ));
    }

    public TemplateEntityResponse getTemplate(Long clientId, Long templateId) {
        return templateEntityRepository.getByIdAndClientId(templateId, clientId)
                .map(template -> templateEntityMapper.toResponse(template, recipientEntityClient))
                .orElseThrow(() -> new TemplateEntityNotFoundException(
                        messageService.getMessage("template.not_found_error", templateId, clientId)
                ));
    }

    public Boolean deleteTemplate(Long clientId, Long templateId) {
        return templateEntityRepository.getByIdAndClientId(templateId, clientId)
                .map(template -> {
                    templateEntityRepository.delete(template);
                    return template;
                })
                .isPresent();
    }

}
