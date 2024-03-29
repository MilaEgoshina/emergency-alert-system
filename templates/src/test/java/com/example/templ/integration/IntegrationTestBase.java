package com.example.templ.integration;


import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc // автоматически настраивает объект MockMvc для выполнения HTTP-запросов в тестах.
@ActiveProfiles("test")
@Testcontainers(parallel = true) // параллельный запуск контейнеров Docker для тестирования.
public class IntegrationTestBase {

    // PostgreSQL контейнер, используемый для запуска базы данных PostgreSQL.
    @Container
    private static final PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:alpine3.17")
    );

    // Kafka контейнер, используемый для запуска Apache Kafka message queue.
    @Container
    private static final GenericContainer<?> messageQueueContainer = new GenericContainer<>(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.2")
    );

    // метод запускает контейнеры перед выполнением всех тестов.
    @BeforeAll
    static void runContainers() {
        databaseContainer.start();
        messageQueueContainer.start();
    }

    // метод для динамической конфигурации свойств приложения, основанных на контейнерах, которые были запущены.
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.kafka.bootstrap-servers", messageQueueContainer::getHost);
    }

    /**
     * Метод для извлечения значений JSON по заданному ключу из результата HTTP-запроса.
     *
     * @param resultActions объект `ResultActions`, содержащий результат выполнения запроса.
     * @param key ключ для извлечения значения.
     * @return возвращает извлеченное значение в строковом представлении.
     */
    public String extractJsonValueByKey(ResultActions resultActions, String key) throws Exception {
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
