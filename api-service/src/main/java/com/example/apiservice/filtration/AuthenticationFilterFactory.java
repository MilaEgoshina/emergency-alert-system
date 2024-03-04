package com.example.apiservice.filtration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Класс - фабрика фильтра для аутентификации и авторизации в Spring Cloud Gateway.
 * Этот класс проверяет подлинность запросов, отправляя запрос проверки с токеном JWT во внутреннюю службу.
 */
@Component
public class AuthenticationFilterFactory extends AbstractGatewayFilterFactory<AuthenticationFilterFactory.Config> {

    private final PathChecker pathChecker;
    private final TokenValidator tokenValidator;

    public AuthenticationFilterFactory(PathChecker pathChecker, TokenValidator tokenValidator) {
        this.pathChecker = pathChecker;
        this.tokenValidator = tokenValidator;
    }

    public AuthenticationFilterFactory(Class<Config> configClass, PathChecker pathChecker, TokenValidator tokenValidator) {
        super(configClass);
        this.pathChecker = pathChecker;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return null;
    }

    public static class Config{

    }
}
