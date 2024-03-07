package com.example.apiservice.filtration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * Класс - фабрика фильтра для аутентификации и авторизации в Spring Cloud Gateway.
 * Этот класс проверяет подлинность запросов, отправляя запрос проверки с токеном JWT во внутреннюю службу.
 */
@Component
public class AuthenticationFilterFactory extends AbstractGatewayFilterFactory<AuthenticationFilterFactory.Config> {

    // определения, требует ли путь запроса аутентификации
    private final PathChecker pathChecker;
    private final TokenValidator tokenValidator;


    public AuthenticationFilterFactory(Class<Config> configClass, PathChecker pathChecker, TokenValidator tokenValidator) {
        super(configClass);
        this.pathChecker = pathChecker;
        this.tokenValidator = tokenValidator;
    }


    /**
     * Метод, который определяет логику фильтра, проверяя, является ли путь запроса защищенным
     * и валидируя токен аутентификации.
     * @param config
     * @return объект GatewayFilter, который является функциональным интерфейсом,
     * принимающим ServerWebExchange и GatewayFilterChain.
     */
    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            if (pathChecker.isProtected.test(exchange.getRequest())) {
                //из заголовков запроса извлекается токен аутентификации (Authorization)
                String token = exchange.getRequest().getHeaders().getFirst("Authorization");
                // Если токен валиден, ServerWebExchange модифицируется путем добавления пользовательского заголовка X-User,
                // содержащего идентификатор пользователя (извлеченный из токена).
                if (tokenValidator.validate(token).isValid()) {
                    exchange = attachUserHeader(exchange, token);
                } else {
                    throw new RuntimeException();
                }
            }

            return chain.filter(exchange);
        };

    }

    /**
     * Вспомогательный метод, который отвечает за добавление к ServerWebExchange
     * заголовка X-User с идентификатором пользователя.
     * @param exchange
     * @param token
     * @return возвращает новый экземпляр ServerWebExchange, который будет использоваться дальше в цепочке фильтров.
     */
    private ServerWebExchange attachUserHeader(ServerWebExchange exchange, String token) {

        // валидации токена и извлечения из него идентификатора пользователя
        String userId = tokenValidator.validate(token).getClientId();
        return exchange.mutate()
                .request(addHeader(exchange, "X-User", userId))
                .build();
    }

    /**
     * Утилитный метод для добавления заголовка в ServerHttpRequest.
     * @param exchange
     * @param name имя заголовка
     * @param value значение заголовка
     * @return возвращает новый экземпляр ServerHttpRequest, который затем используется для создания
     * нового ServerWebExchange в методе attachUserHeader.
     */
    private ServerHttpRequest addHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.getRequest()// получение текущего запроса из ServerWebExchange
                .mutate() // модифицирует этот запрос, вызывая mutate() для создания его изменяемой копии.
                .header(name, value) // добавление заголовка с указанными именем и значением
                .build();
    }

    public static class Config {

    }

}

