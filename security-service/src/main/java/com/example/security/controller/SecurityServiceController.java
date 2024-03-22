package com.example.security.controller;

import com.example.security.dto.request.SecurityServiceRequest;
import com.example.security.dto.response.AuthTokenResponse;
import com.example.security.entity.Customer;
import com.example.security.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * Класс - контроллер для обработки операций аутентификации.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SecurityServiceController {


    private final CustomerService customerService;

    /**
     * Конечная точка для регистрации нового клиента с предоставленными учетными данными.
     * @param securityRequest Объект SecurityRequest, содержащий данные для регистрации.
     * @return возвращает экземпляр класса ResponseEntity, указывающий на успешность операции.
     */
    @PostMapping("/register")
    @Operation(summary = "Зарегистрировать нового клиента с предоставленными учетными данными")
    public ResponseEntity<Boolean> registerNewCustomer(
            @RequestBody @Valid SecurityServiceRequest securityRequest
    ) {
        return ResponseEntity.ok(customerService.registerNewCustomer(securityRequest));
    }

    /**
     * Конечная точка для аутентификации клиента с существующими учетными данными.
     * @param securityRequest Объект SecurityRequest, содержащий данные для аутентификации.
     * @return возвращает экземпляр класса ResponseEntity, содержащий токен аутентификации.
     */
    @PostMapping("/authenticate")
    @Operation(summary = "Аутентификация клиента с существующими учетными данными")
    public ResponseEntity<AuthTokenResponse> authenticateClient(
            @RequestBody @Valid SecurityServiceRequest securityRequest
    ) {
        return ResponseEntity.ok(customerService.authenticateCustomer(securityRequest));
    }

    /**
     * Конечная точка для проверки JWT и возврата ID клиента.
     * @param customer аутентифицированный клиент.
     * @return возвращает экземпляр класса ResponseEntity, содержащий ID клиента.
     */
    @GetMapping("/validate")
    @Operation(summary = "Проверка предоставленного JWT и возврат ID клиента")
    public ResponseEntity<Long> validateToken(
            @AuthenticationPrincipal Customer customer
    ) {
        return ResponseEntity.ok(customer.getId());
    }
}
