package com.example.recipient.tests;

import com.example.recipient.builder.RecipientEntityBuilderTest;
import com.example.recipient.builder.RecipientEntityTest;
import com.example.recipient.configuration.IntegrationTestBase;
import com.example.recipient.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Класс для интеграционного тестирования контроллера, отвечающего за работу с сущностью RecipientEntity
 */
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class RecipientEntityControllerTest extends IntegrationTestBase {

    // используется для отправки mock-запросов и проверки ответов контроллера.
    private final MockMvc mockMvc;
    private final MessageService messageService;

    static final RecipientEntityTest FIRST_RECIPIENT = RecipientEntityBuilderTest.builder()
            .email("egorov@gmal.com")
            .phoneNumber("+1234567890")
            .telegramId("egorov123")
            .build();

    static final RecipientEntityTest SECOND_RECIPIENT = RecipientEntityBuilderTest.builder()
            .email("ivanov@gmail.com")
            .phoneNumber("+0987654321")
            .build();

    static final RecipientEntityTest REPEATED_PHONE_RECIPIENT = RecipientEntityBuilderTest.builder()
            .email("misha@gmail.com")
            .phoneNumber("+0987654321")
            .telegramId("misha123")
            .build();

    static final RecipientEntityTest REPEATED_EMAIL_RECIPIENT = RecipientEntityBuilderTest.builder()
            .email("egorov@gmal.com")
            .telegramId("user5678")
            .build();
}
