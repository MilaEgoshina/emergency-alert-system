package com.example.links.controller;

import com.example.links.dto.request.MessageOptionsRequest;
import com.example.links.dto.response.LinkEntityResponse;
import com.example.links.service.ResponseEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responses")
public class ResponseEntityController {

    private final ResponseEntityService responseEntityService;

    @PostMapping("/")
    public ResponseEntity<Long> create(
            @RequestBody @Valid MessageOptionsRequest optionsRequest
    ) {
        return ResponseEntity.status(CREATED).body(responseEntityService.createResponseEntity(optionsRequest));
    }

    @PostMapping("/generate/{id}")
    public ResponseEntity<LinkEntityResponse> generate(
            @PathVariable("id") Long responseId
    ) {
        return ResponseEntity.status(CREATED).body(responseEntityService.generateResponse(responseId));
    }
}
