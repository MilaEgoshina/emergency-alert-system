package com.example.messaging.config;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

// пользовательская аннотация для обозначения интеграционных тестов.
@MessageIntegrationTest
// аннотация для выполнения SQL-скрипта data.sql перед каждым тестом.
@Sql({
        "classpath:sql/data.sql"
})
public class MessageIntegrationTestBase {

    // статическая переменная, которая создает и хранит экземпляр контейнера PostgreSQL.
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:alpine3.17");

    // запуск контейнера PostgreSQL
    @BeforeAll
    static void startPostgresContainer() {
        postgreSQLContainer.start();
    }

    // аннотация, означающая использование метода для динамического конфигурирования свойств в тестах Spring.
    @DynamicPropertySource
    static void configureDynamicProperties(DynamicPropertyRegistry registry) {

        // добавление свойства spring.datasource.url в контекст теста, используя URL-адрес базы данных,
        // полученный из контейнера PostgreSQL (postgreSQLContainer.getJdbcUrl()).
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    /**
     * Метод для извлечения значения по ключу из JSON-ответа, полученного в результате выполнения HTTP-запроса.
     * @param resultActions результат выполнения запроса.
     * @param key ключ для извлечения значений.
     * @return возвращает строковое представление ответа.
     */
    public String extractValueByKey(ResultActions resultActions, String key) throws Exception {
        return new ObjectMapper()
                .readTree(
                        resultActions.andReturn()
                                .getResponse()
                                .getContentAsString()
                )
                .at("/" + key)
                .asText();
    }
}
