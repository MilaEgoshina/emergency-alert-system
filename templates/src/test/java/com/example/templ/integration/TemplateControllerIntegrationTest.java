package com.example.templ.integration;

import com.example.templ.builder.RecipientListEntityJson;
import com.example.templ.builder.TemplateEntityJson;
import com.example.templ.builder.TemplateEntityJsonBuilderTest;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class TemplateControllerIntegrationTest extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final MessageService messageService;

    static final Long CLIENT_ID = 1L;

    static final TemplateEntityJson TEMPLATE = TemplateEntityJsonBuilderTest.builder()
            .content("content")
            .title("title")
            .build();

    static final RecipientListEntityJson RECIPIENT_LIST_JSON = new RecipientListEntityJson(List.of(1, 2, 3, 1));

    @MockBean
    private final RecipientEntityClient recipientEntityClient;

    @BeforeEach
    void setUp() {
        RecipientEntityClientMock.setupRecipientEntityClientMock(recipientEntityClient);
    }

    @Test
    public void createTemplateTest() throws Exception {
        Long id = createTemplateCreated(TEMPLATE);
        assertThat(id).isPositive();

        createTemplateConflict(TEMPLATE);
    }

    @Test
    public void getTemplateTest() throws Exception {
        getTemplateNotFound(1L);

        Long id = createTemplateCreated(TEMPLATE);

        getTemplateOk(id, TEMPLATE);
    }

    @Test
    public void deleteTemplateTest() throws Exception {
        deleteTemplateAndExpectResult(1L, false);

        Long id = createTemplateCreated(TEMPLATE);
        getTemplateOk(id, TEMPLATE);

        deleteTemplateAndExpectResult(id, true);
        getTemplateNotFound(id);
    }

    @Test
    public void addRecipientsTest() throws Exception {
        addRecipientsTemplateNotFound(1L, RECIPIENT_LIST_JSON);

        Long id = createTemplateCreated(TEMPLATE);
        getTemplateOk(id, TEMPLATE);

        addRecipientsTemplateCreated(id, RECIPIENT_LIST_JSON, TEMPLATE);
        getTemplateOk(id, TEMPLATE);
    }

    @Test
    public void deleteRecipientsTest() throws Exception {
        deleteRecipientsTemplateNotFound(1L, RECIPIENT_LIST_JSON);

        Long id = createTemplateCreated(TEMPLATE);
        getTemplateOk(id, TEMPLATE);

        addRecipientsTemplateCreated(id, new RecipientListEntityJson(List.of(1)), TEMPLATE);
        deleteRecipientsTemplateOk(id, RECIPIENT_LIST_JSON, TEMPLATE);
        getTemplateOk(id, TEMPLATE);
    }

    private Long createTemplateCreated(TemplateEntityJson template) throws Exception {
        ResultActions result = mockMvc.perform(post(LinksEnums.CREATE_TEMPLATE.getLinks())
                        .header("clientId", CLIENT_ID)
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

    private void createTemplateConflict(TemplateEntityJson template) throws Exception {
        mockMvc.perform(post(LinksEnums.CREATE_TEMPLATE.getLinks())
                        .header("clientId", CLIENT_ID)
                        .content(template.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.title_already_exists", template.title(), CLIENT_ID)
                        )
                );
    }

    private void getTemplateNotFound(Long templateId) throws Exception {
        mockMvc.perform(get(LinksEnums.GET_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    private void getTemplateOk(Long templateId, TemplateEntityJson template) throws Exception {
        mockMvc.perform(get(LinksEnums.GET_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
    }

    private void deleteTemplateAndExpectResult(Long templateId, Boolean result) throws Exception {
        mockMvc.perform(delete(LinksEnums.DELETE_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value(result)
                );
    }

    private void addRecipientsTemplateNotFound(Long templateId, RecipientListEntityJson recipients) throws Exception {
        mockMvc.perform(post(LinksEnums.ADD_RECIPIENT.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    private void addRecipientsTemplateCreated(Long templateId, RecipientListEntityJson recipients, TemplateEntityJson template) throws Exception {
        mockMvc.perform(post(LinksEnums.ADD_RECIPIENT.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isArray()
                );
    }

    private void deleteRecipientsTemplateNotFound(Long templateId, RecipientListEntityJson recipients) throws Exception {
        mockMvc.perform(delete(LinksEnums.DELETE_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    private void deleteRecipientsTemplateOk(Long templateId, RecipientListEntityJson recipients, TemplateEntityJson template) throws Exception {
        mockMvc.perform(delete(LinksEnums.DELETE_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isArray()
                );
    }

}
