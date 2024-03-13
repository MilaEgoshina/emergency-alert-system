package com.example.recipient.configuration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Базовый класс для интеграционных тестов.
 */
@IntegrationTest
//указывает, что перед выполнением теста необходимо выполнить SQL-скрипт data.sql для инициализации данных в тестовой базе данных.
@Sql({
        "classpath:sql/data.sql"
})
public class IntegrationTestBase {

    // экземпляр контейнера PostgreSQL, созданный с помощью Testcontainers.
    // Он используется для запуска PostgreSQL в контейнере Docker для тестов.
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine3.17");

    // запуск контейнера PostgreSQL перед выполнением тестов.
    @BeforeAll
    static void runContainer() {
        postgreSQLContainer.start();
    }

    /**
     * Метод, помеченный @DynamicPropertySource, который добавляет URL подключения к PostgreSQL в контейнере в качестве
     * свойства Spring для тестов.
     */
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    /**
     * Вспомогательный метод, который извлекает значение JSON-поля из результатов запроса, выполненного с
     * помощью MockMvc (Spring MVC Test).
     */
    public String extractJsonValueByKey(ResultActions resultActions, String field) throws Exception {
        return new ObjectMapper()
                .readTree(
                        resultActions.andReturn()
                                .getResponse()
                                .getContentAsString()
                )
                .at("/" + field)
                .asText();
    }
}
