package com.example.recipient.configuration;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Пользовательская аннотация, которая объединяет несколько других аннотаций для настройки интеграционных тестов.
 */

// указывает, что эта аннотация может применяться только к классам.
@Target(ElementType.TYPE)
// означает, что аннотация будет доступна во время выполнения программы.
@Retention(RetentionPolicy.RUNTIME)
// указывает, что тесты должны запускаться с профилем "test".
@ActiveProfiles("test")
// гарантирует, что каждый тест будет выполняться в транзакции, что упрощает управление данными в тестах.
@Transactional
// загружает контекст Spring Boot для тестов, используя класс RecipientServiceApplicationTest в качестве входной точки.
@SpringBootTest(classes = RecipientServiceApplicationTest.class)
public @interface IntegrationTest {
}
