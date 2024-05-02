package com.example.security.service;

import com.example.security.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Класс отвечает за обработку выхода пользователя из системы, включая удаление токена и очистку контекста безопасности.
 */
@Service
@RequiredArgsConstructor
public class CustomLogOutHandler implements LogoutHandler {

    private final AuthTokenService authTokenService;

    private final CustomerService customerService;

    private final JwtService jwtService;

    /**
     * Выполняет процесс выхода пользователя из системы.
     *
     * @param request объект HttpServletRequest, который содержит информацию о запросе, включая заголовки.
     * @param response объект HttpServletResponse, используется для отправки ответа клиенту.
     * @param authentication объект Authentication, представляющий аутентифицированного пользователя.
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {

        // извлечение JWT токена из запроса:
        String jwtToken = jwtService.extractToken(request);

        if (jwtToken != null && !jwtToken.isEmpty()) {
            String customerEmail = jwtService.extractEmail(jwtToken); // Получение email пользователя из JWT токена.
            Customer customer = customerService.loadUserByUsername(customerEmail); // загрузка данных клиента по email.

            authTokenService.deletePreviousTokenForCustomer(customer); // удаление предыдущего токена для данного клиента.
            SecurityContextHolder.clearContext(); // очищение контекста безопасности, чтобы пользователь был разлогинен.
        }
    }
}
