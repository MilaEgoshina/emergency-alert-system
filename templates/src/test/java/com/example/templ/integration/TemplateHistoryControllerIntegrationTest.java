package com.example.templ.integration;

import com.example.templ.builder.TemplateEntityJson;
import com.example.templ.client.RecipientEntityClient;
import com.example.templ.config.LinksEnums;
import com.example.templ.mockstest.RecipientEntityClientMock;
import com.example.templ.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class TemplateHistoryControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final MessageService messageService;

    @MockBean
    private final RecipientEntityClient recipientEntityClient;

    @BeforeEach
    void setUp() {
        RecipientEntityClientMock.setupRecipientEntityClientMock(recipientEntityClient);
    }

    @Test
    public void createTemplateHistoryTest() throws Exception {
        createTemplateHistoryNotFound(1L);
        Long templateId = createTemplateCreated(TemplateControllerIntegrationTest.TEMPLATE);
        createTemplateHistoryCreated(templateId, TemplateControllerIntegrationTest.TEMPLATE);
        createTemplateHistoryCreated(templateId, TemplateControllerIntegrationTest.TEMPLATE);
    }

    @Test
    public void getTemplateHistoryTest() throws Exception {
        getTemplateHistoryNotFound(1L);
        Long templateId = createTemplateCreated(TemplateControllerIntegrationTest.TEMPLATE);
        createTemplateHistoryCreated(templateId, TemplateControllerIntegrationTest.TEMPLATE);
        getTemplateHistoryOk(templateId, TemplateControllerIntegrationTest.TEMPLATE);
        getTemplateHistoryOk(templateId, TemplateControllerIntegrationTest.TEMPLATE);
    }

    private void createTemplateHistoryCreated(Long templateId, TemplateEntityJson template) throws Exception {
        mockMvc.perform(post(LinksEnums.CREATE_HISTORY.getLinks().formatted(templateId))
                        .header("clientId",TemplateControllerIntegrationTest.CLIENT_ID))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty()
                );
    }

    private void createTemplateHistoryNotFound(Long templateId) throws Exception {
        mockMvc.perform(post(LinksEnums.CREATE_HISTORY.getLinks().formatted(templateId))
                        .header("clientId", TemplateControllerIntegrationTest.CLIENT_ID))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, TemplateControllerIntegrationTest.CLIENT_ID)
                        )
                );
    }

    private void getTemplateHistoryOk(Long templateId, TemplateEntityJson template) throws Exception {
        mockMvc.perform(get(LinksEnums.GET_HISTORY.getLinks().formatted(templateId))
                        .header("clientId", TemplateControllerIntegrationTest.CLIENT_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty()
                );
    }

    private void getTemplateHistoryNotFound(Long historyId) throws Exception {
        mockMvc.perform(get(LinksEnums.GET_HISTORY.getLinks().formatted(historyId))
                        .header("clientId", TemplateControllerIntegrationTest.CLIENT_ID))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("history.not_found", historyId, TemplateControllerIntegrationTest.CLIENT_ID)
                        )
                );
    }

    private Long createTemplateCreated(TemplateEntityJson template) throws Exception {
        ResultActions result = mockMvc.perform(post(LinksEnums.CREATE_TEMPLATE.getLinks())
                        .header("clientId", TemplateControllerIntegrationTest.CLIENT_ID)
                        .content(template.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
        return Long.valueOf(extractJsonValueByKey(result, "id"));
    }
}
