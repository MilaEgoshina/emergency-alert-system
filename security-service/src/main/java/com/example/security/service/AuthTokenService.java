package com.example.security.service;

import com.example.security.entity.AuthTokenEntity;
import com.example.security.entity.Customer;
import com.example.security.exception.authtoken.FailedAuthTokenException;
import com.example.security.exception.customer.CustomerJwtNotFoundException;
import com.example.security.exception.customer.CustomerNotFoundException;
import com.example.security.repository.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Класс отвечает за генерацию новых токенов, удаление предыдущих токенов для клиента, валидацию токенов и извлечение информации о клиенте из JWT.
 */
@Service
// гарантия создания всех необходимых зависимостей через конструктор и гарантия "ленивой" инициализации зависимостей.
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AuthTokenService {

    private final JwtService jwtService; // сервис для работы с JWT (JSON Web Token).
    private final CustomerService customerService; // сервис для работы с информацией о клиентах.
    private final AuthTokenRepository authTokenRepository; // репозиторий для работы с сущностью токена аутентификации.
    private final CustomMessageSourceService messageSourceService;

    /**
     * Метод для генерации нового токена для указанного клиента и сохранение его в репозитории.
     *
     * @param customer объект клиента, для которого создается токен.
     * @param token JWT.
     */
    public void generateNewToken(Customer customer, String token) {
        authTokenRepository.save(AuthTokenEntity.builder()
                .customer(customer)
                .tokenValue(token)
                .isExpired(false)
                .isRevoked(false)
                .build());
    }

    /**
     * Метод для удаления предыдущего токена для указанного клиента.
     *
     * @param customer клиент, для которого необходимо удалить токен.
     */
    public void deletePreviousTokenForCustomer(Customer customer) {
        authTokenRepository.getByClientId(customer.getId())
                .ifPresent(authTokenRepository::delete);
    }

    /**
     * Метод для проверки валидности токена.
     *
     * @param token переданный токен для проверки его валидности.
     * @return если токен действителен и JWT корректен, возвращает true, в противном случае выбрасывает исключение
     * InvalidTokenException.
     */
    public boolean validateToken(String token) {

        Customer customer = extractClientDetailsFromToken(token); // извлечение информации о клиенте из JWT.

        // проверка токена в репозитории на актуальность и отозванность.
        boolean isTokenValid = authTokenRepository.getByJwt(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (isTokenValid && jwtService.isTokenValid(token, customer)) {
            return true;
        } else {
            throw new FailedAuthTokenException(messageSourceService.getMessage("token.invalid"));
        }
    }

    /**
     * Метод для получения информации о клиенте из JWT.
     *
     * @param token JWT, из которого нужно извлечь информацию.
     * @return возвращает объект Client, представляющий пользователя, чьи данные были извлечены из JWT.
     */
    public Customer extractClientDetailsFromToken(String token) {
        String email = jwtService.extractEmail(token);
        try {
            return customerService.loadUserByUsername(email);
        } catch (CustomerNotFoundException c) {
            throw new CustomerJwtNotFoundException(c.getMessage());
        }
    }
}
