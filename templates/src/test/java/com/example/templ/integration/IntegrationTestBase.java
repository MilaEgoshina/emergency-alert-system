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
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers(parallel = true)
public class IntegrationTestBase {


    @Container
    private static final PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:alpine3.17")
    );

    @Container
    private static final GenericContainer<?> messageQueueContainer = new GenericContainer<>(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.2")
    );

    @BeforeAll
    static void runContainers() {
        databaseContainer.start();
        messageQueueContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.kafka.bootstrap-servers", messageQueueContainer::getHost);
    }

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
