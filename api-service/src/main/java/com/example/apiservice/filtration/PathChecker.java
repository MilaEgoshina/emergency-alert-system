package com.example.apiservice.filtration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * Класс - компонент, используется для проверки того, следует ли считать конкретный путь входящего HTTP - запроса безопасным или нет.
 */
@Component
public class PathChecker {

    //список, который содержит URI открытых API эндпоинтов.
    public static final List<String> publicEndpoints = List.of("/swagger-ui","/api-docs");

    public Predicate<ServerHttpRequest> isSecured = this::isNotPublic;

    /**
     * Метод для проверки - является ли запрос защищенным или публичным.
     * @param request входящий запрос
     * @return true, если запрос НЕ к открытым API точкам. Иначе вернёт false.
     */
    private boolean isNotPublic(ServerHttpRequest request) {
        return !publicEndpoints.contains(request.getURI().getPath());
    }

}
