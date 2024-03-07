package com.example.apiservice.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class ApplicationConfig {

    /**
     *
     * Метод, который создает компонент типа HttpMessageConverters и отвечает за преобразование тел
     * HTTP-сообщений, т.е. сериализует/десериализует данные JSON в объекты Java и обратно.
     * @return экземпляр HttpMessageConverters, который в дальнейшем используется для конвертации HTTP-сообщений
     */
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters(new GsonHttpMessageConverter());
    }

    /**
     * Метод, который определяет бин CorsWebFilter и используется для настройки политики
     * CORS (Cross-Origin Resource Sharing)
     * @return CorsWebFilter
     */
    @Bean
    public CorsWebFilter corsWebFilterConfig() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        // установка разрешенных источников и разрешенных HTTP-методов (DELETE и PATCH).
        corsConfig.applyPermitDefaultValues();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8000", "http://localhost:3000"));
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.PATCH);

        // связываем созданную конфигурацию CORS с URL-паттерном /** (все пути).
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    /**
     * Метод определяет бин RestTemplate для выполнения HTTP-запросов и получения ответов.
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restOperationsTemplate() {
        return new RestTemplate();
    }
}
