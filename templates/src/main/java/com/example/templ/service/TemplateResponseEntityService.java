package com.example.templ.service;


import com.example.templ.client.LinkShortenerClient;
import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.request.MessageOptionsRequest;
import com.example.templ.dto.response.TemplateEntityResponse;
import com.example.templ.exception.templatexcpetions.TemplateEntityNotFoundException;
import com.example.templ.mapper.TemplateEntityMapper;
import com.example.templ.repository.TemplateEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateResponseEntityService {

    private final TemplateEntityRepository templateEntityRepository;

    private final RecipientEntityClient recipientEntityClient;

    private final LinkShortenerClient linkShortenerClient;

    private final MessageService messageService;

    private final TemplateEntityMapper templateEntityMapper;

    public TemplateEntityResponse setResponseOptions(Long clientId, Long templateId, MessageOptionsRequest messageRequest) {
        return templateEntityRepository.getByIdAndClientId(templateId, clientId)
                .map(template -> template.setResponse(linkShortenerClient.generate(messageRequest).getBody()))
                .map(templateEntityRepository::saveAndFlush)
                .map(template -> templateEntityMapper.toResponse(template, recipientEntityClient))
                .orElseThrow(() -> new TemplateEntityNotFoundException(
                        messageService.getMessage("template.not_found_error", templateId, clientId)
                ));
    }

}
