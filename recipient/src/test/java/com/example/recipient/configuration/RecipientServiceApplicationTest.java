package com.example.recipient.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

/**
 * Конфигурационный класс для тестового контекста Spring Boot.
 */
@TestConfiguration
@ActiveProfiles("test")
public class RecipientServiceApplicationTest {

    // Этот класс используется в качестве входной точки для загрузки контекста Spring Boot во время тестов.
}
