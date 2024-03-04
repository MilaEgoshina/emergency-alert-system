package com.example.apiservice.filtration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Класс - валидатор JWT токенов с использованием внешнего сервиса верификации.
 */
@Component
public class TokenValidator {

    // URL для проверки токенов
    private final String validateURL;

    // клиент для внешних HTTP вызовов
    private final RestTemplate restTemplate;


    public TokenValidator(@Value("${urls.validate}") String validateURL, RestTemplate restTemplate) {
        this.validateURL = validateURL;
        this.restTemplate = restTemplate;
    }

    /**
     * Метод валидации токенов
     * @param token токен для валидации
     * @return возвращается полученный из ответа результат валидации токена
     */
    public TokenValidationResponse validate(String token) {

        // создание HTTP заголовков с переданным токеном
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        // формируется HTTP запрос по URL валидации с этими заголовками
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(validateURL));
        // запрос выполняется через restTemplate
        ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(request, TokenValidationResponse.class);

        return response.getBody();
    }

}
