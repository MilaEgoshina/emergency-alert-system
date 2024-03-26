package com.example.templ.service;

import com.example.templ.dto.response.TemplateHistoryEntityResponse;
import com.example.templ.entity.TemplateEntity;
import com.example.templ.entity.TemplateHistoryEntity;
import com.example.templ.exception.templatehistory.TemplateHistoryNotFoundException;
import com.example.templ.exception.templatexcpetions.TemplateEntityNotFoundException;
import com.example.templ.mapper.TemplateEntityMapper;
import com.example.templ.repository.TemplateEntityHistoryRepository;
import com.example.templ.repository.TemplateEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateHistoryEntityService {

    private final TemplateEntityRepository templateEntityRepository;

    private final TemplateEntityHistoryRepository templateHistoryRepository;

    private final TemplateEntityMapper templateEntityMapper;

    private final MessageService messageService;

    public TemplateHistoryEntityResponse createTemplateHistory(Long clientId, Long templateId) {
        TemplateEntity template = templateEntityRepository.getByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateEntityNotFoundException(
                        messageService.getMessage("template.not_found_error", templateId, clientId)
                ));

        Optional<TemplateHistoryEntity> optTemplateHistory = templateHistoryRepository.getByClientIdAndResponseIdAndHeaderAndDetails(
                clientId,
                template.getResponseId(),
                template.getTemplateTitle(),
                template.getTemplateContent()
        );
        if (optTemplateHistory.isPresent()) {
            return templateEntityMapper.toTemplateHistoryResponse(optTemplateHistory.get());
        }

        return Optional.of(template)
                .map(templateEntityMapper::toTemplateHistory)
                .map(templateHistory -> templateHistory.assignClient(clientId))
                .map(templateHistoryRepository::saveAndFlush)
                .map(templateEntityMapper::toTemplateHistoryResponse)
                .orElseThrow(() -> new TemplateHistoryNotFoundException(
                        messageService.getMessage("template-history.creation_error", templateId)
                ));
    }

    public TemplateHistoryEntityResponse getTemplateHistory(Long clientId, Long templateHistoryId) {
        return templateHistoryRepository.getByIdAndClientId(templateHistoryId, clientId)
                .map(templateEntityMapper::toTemplateHistoryResponse)
                .orElseThrow(() -> new TemplateHistoryNotFoundException(
                        messageService.getMessage("template-history.not_found_error", templateHistoryId, clientId)
                ));
    }


}
