package com.example.links.service;

import com.example.links.dto.request.MessageOptionsRequest;
import com.example.links.dto.response.LinkEntityResponse;
import com.example.links.entity.LinkEntity;
import com.example.links.entity.ResponseEntity;
import com.example.links.mapper.ResponseEntityMapper;
import com.example.links.repository.LinkEntityRepository;
import com.example.links.repository.ResponseEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseEntityService {

    private final ResponseEntityRepository responseEntityRepository;

    private final LinkEntityRepository linkEntityRepository;

    private final ResponseEntityMapper responseEntityMapper;

    public Long createResponseEntity(MessageOptionsRequest request) {
        return Optional.of(request)
                .map(responseEntityMapper::toResponse)
                .map(responseEntityRepository::saveAndFlush)
                .map(ResponseEntity::getId)
                .orElseThrow();
    }

    public LinkEntityResponse generateResponse(Long responseId) {
        Map<String, String> linksOptionMap = responseEntityRepository.findById(responseId)
                .map(ResponseEntity::getOptions)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        strings -> UUID.randomUUID().toString(),
                        Function.identity()
                ));

        LinkEntity link = LinkEntity.builder()
                .linksOptionMap(linksOptionMap)
                .build();

        LinkEntity saveLink = linkEntityRepository.save(link);
        return new LinkEntityResponse(
                saveLink.getId(),
                linksOptionMap
        );
    }
}
