package com.example.templ.integration;

import com.example.templ.builder.TemplateEntityJson;
import com.example.templ.client.RecipientEntityClient;
import com.example.templ.mockstest.RecipientEntityClientMock;
import com.example.templ.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.example.templ.config.LinksEnums.*;
import static com.example.templ.integration.TemplateControllerIntegrationTest.CLIENT_ID;
import static com.example.templ.integration.TemplateControllerIntegrationTest.TEMPLATE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Класс для тестирования контроллера TemplateHistoryEntityController, которые помогают убедиться в правильной работе функциональности
 * создания и получения истории шаблонов через HTTP запросы.
 */
@RequiredArgsConstructor
public class TemplateHistoryControllerIntegrationTest {

    private final MockMvc mockMvc; // для выполнения HTTP запросов к контроллеру без реального запуска приложения.
    private final MessageService messageService;

    @MockBean
    private final RecipientEntityClient recipientEntityClient; // мок-бин для взаимодействия с клиентом получателя.

    @BeforeEach
    void setUp() {
        RecipientEntityClientMock.setupRecipientEntityClientMock(recipientEntityClient);
    }

    @Test
    public void createTemplateHistoryTest() throws Exception {
        createTemplateHistoryWhichIsNotFound(1L); // попытка создать историю для несуществующего шаблона
        Long templateId = createTemplate(TEMPLATE_ENTITY);

        // Создается история для этого шаблона два раза:
        createTemplateHistorySuccess(templateId, TEMPLATE_ENTITY);
        createTemplateHistorySuccess(templateId, TEMPLATE_ENTITY);
    }

    @Test
    public void getTemplateHistoryTest() throws Exception {

        // попытка получения истории для несуществующего шаблона
        getTemplateHistoryWhichIsNotFound(1L);
        Long templateId = createTemplate(TEMPLATE_ENTITY);

        // создание истории для этого шаблона:
        createTemplateHistorySuccess(templateId, TEMPLATE_ENTITY);

        // попытка дважды получить историю для этого шаблона:
        getTemplateHistorySuccessfully(templateId, TEMPLATE_ENTITY);
        getTemplateHistorySuccessfully(templateId, TEMPLATE_ENTITY);
    }

    /**
     * Метод для создания истории шаблона для заданного id шаблона и проверки ожидаемых полей и значений в ответе.
     *
     * @param templateId идентификатор шаблона.
     * @param templateEntityJson объект для создания шаблона сообщения.
     */
    private void createTemplateHistorySuccess(Long templateId, TemplateEntityJson templateEntityJson) throws Exception {

        //POST запрос на URL CREATE_HISTORY с указанным id шаблона и заголовком clientId.
        mockMvc.perform(post(CREATE_HISTORY.getLinks().formatted(templateId))
                        .header("clientId",CLIENT_ID))
                .andExpectAll( // Ожидается код состояния 'Created' (201).

                        // Проверяются значения полей в ответе: id, title, content, imageUrl.
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.header").value(templateEntityJson.templateTitle()),
                        jsonPath("$.details").value(templateEntityJson.templateContent()),
                        jsonPath("$.linkUrl").isEmpty()
                );
    }

    // метод проверяет ответ в случае, если шаблон не найден.
    private void createTemplateHistoryWhichIsNotFound(Long templateId) throws Exception {
        mockMvc.perform(post(CREATE_HISTORY.getLinks().formatted(templateId))
                        .header("clientId",CLIENT_ID))
                .andExpectAll( // Ожидается код состояния 'Not Found' (404).

                        // Проверяется сообщение об ошибке в ответе.
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    //  метод получает историю шаблона по заданному id и проверяет ожидаемые поля и значения в ответе.
    private void getTemplateHistorySuccessfully(Long templateId, TemplateEntityJson template) throws Exception {
        mockMvc.perform(get(GET_HISTORY.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(), // Ожидается код состояния 'Ok' (200).

                        // Проверяются значения полей в ответе: id, title, content, imageUrl.
                        jsonPath("$.id").exists(),
                        jsonPath("$.header").value(template.templateTitle()),
                        jsonPath("$.details").value(template.templateContent()),
                        jsonPath("$.linkUrl").isEmpty()
                );
    }

    // метод проверяет ответ в случае, если история шаблона не найдена.
    private void getTemplateHistoryWhichIsNotFound(Long historyId) throws Exception {
        mockMvc.perform(get(GET_HISTORY.getLinks().formatted(historyId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isNotFound(), // Ожидается код состояния 'Not Found' (404).

                        // Проверяется сообщение об ошибке в ответе.
                        jsonPath("$.message").value(
                                messageService.getMessage("history.not_found", historyId,CLIENT_ID)
                        )
                );
    }

    // метод создает новый шаблон и возвращает его id.
    private Long createTemplate(TemplateEntityJson template) throws Exception {

        //Осуществляется POST запрос на URL CREATE_TEMPLATE с данными шаблона и заголовком clientId.
        ResultActions result = mockMvc.perform(post(CREATE_TEMPLATE.getLinks())
                        .header("clientId", CLIENT_ID)
                        .content(template.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(), // Ожидается код состояния 'Created' (201).

                        // Проверяются значения полей в ответе: id, title, content, imageUrl, recipientIds.
                        jsonPath("$.id").exists(),
                        jsonPath("$.header").value(template.templateTitle()),
                        jsonPath("$.details").value(template.templateContent()),
                        jsonPath("$.linkUrl").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
        return Long.valueOf(extractJsonValueByKey(result, "id"));
    }
    private String extractJsonValueByKey(ResultActions resultActions, String key) throws IOException {

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
