package com.example.security.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.UnsupportedEncodingException;

@Transactional // автоматический откат транзакций после каждого теста.
@SpringBootTest
@AutoConfigureMockMvc // настройка MockMvc для отправки HTTP-запросов.
@ActiveProfiles("test")
@Testcontainers(parallel = true)
@Sql({
        "classpath:sql/seed_data.sql" // выполнение SQL-скрипта для начальной загрузки данных перед тестами.
})
public class BaseIntegrationTest {

    @Container // создание контейнера PostgreSQL с использованием библиотеки Testcontainers.
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:alpine3.17") // инициализация контейнера
    );

    @BeforeAll // контейнер должен запуститься перед запуском тестов.
    static void startContainer() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource // динамическая настройка URL подключения к PostgreSQL контейнеру.
    static void configurePostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    /**
     * Метод для извлечения значения определенного поля из JSON-ответа, полученного в результате выполнения HTTP-запроса.
     *
     * @param resultActions объект ResultActions, представляющий результат выполнения запроса с помощью Spring MockMvc.
     * @param key строка, представляющая ключ, значение которого необходимо извлечь из JSON-ответа.
     * @return Возвращается извлеченное значение в виде строки.
     */
    public String extractJsonValueByKey(ResultActions resultActions, String key) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree( // анализ структуры JSON-ответа.
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString() // получение содержимого (тело) JSON-ответа, преобразовывая его в строку.
        );
        return jsonNode.at("/" + key).asText(); // навигация по JSON-дереву к указанному полю.
    }
}
