package com.example.templ.integration;

import com.example.templ.builder.RecipientListEntityJson;
import com.example.templ.builder.TemplateEntityJson;
import com.example.templ.builder.TemplateEntityJsonBuilderTest;
import com.example.templ.client.RecipientEntityClient;
import com.example.templ.mockstest.RecipientEntityClientMock;
import com.example.templ.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.example.templ.config.LinksEnums.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционный тест для контроллера TemplateEntityController.
 */
@RequiredArgsConstructor
public class TemplateControllerIntegrationTest extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final MessageService messageService;

    static final Long CLIENT_ID = 1L;

    static final TemplateEntityJson TEMPLATE_ENTITY = TemplateEntityJsonBuilderTest.builder()
            .templateContent("templateContent")
            .templateTitle("templateTitle")
            .build();

    static final RecipientListEntityJson RECIPIENT_LIST_ENTITY_JSON = new RecipientListEntityJson(List.of(1, 2, 3, 1));

    @MockBean
    private final RecipientEntityClient recipientEntityClient;

    @BeforeEach
    void setUp() {
        RecipientEntityClientMock.setupRecipientEntityClientMock(recipientEntityClient);
    }

    /**
     * Метод для тестирования создания шаблона.
     */
    @Test
    public void createTemplateEntityTest() throws Exception {

        Long id = templateEntityCreation(TEMPLATE_ENTITY); //создание шаблонов
        assertThat(id).isPositive();// проверка успешного создания шаблона

        templateEntityCreationConflict(TEMPLATE_ENTITY);// проверка конфликта при создании шаблона
    }

    /**
     * Метод для тестирования получения шаблона по его идентификатору.
     */
    @Test
    public void receiveTemplateEntityTest() throws Exception {
        getTemplateEntityNotFound(1L); // проверка, что шаблон не найден.

        Long id = templateEntityCreation(TEMPLATE_ENTITY); // создание шаблона.

        getTemplateEntitySuccess(id, TEMPLATE_ENTITY); // проверка успешного получения шаблона.
    }

    /**
     * Метод для тестирования удаления шаблона.
     */
    @Test
    public void deleteTemplateEntityTest() throws Exception {
        deleteTemplateEntityAndExpectResult(1L, false); //проверка результата удаления шаблона.

        Long id = templateEntityCreation(TEMPLATE_ENTITY); // создание шаблона.
        getTemplateEntitySuccess(id, TEMPLATE_ENTITY); // проверка успешного получения шаблона.

        deleteTemplateEntityAndExpectResult(id, true); //проверка результата удаления шаблона.
        getTemplateEntityNotFound(id); // в случае успешного удаления идентификатор удаленного шаблона не должен быть найден.
    }

    /**
     * Метод для тестирования добавления получателей к шаблону.
     */
    @Test
    public void addRecipientsToTemplateTest() throws Exception {
        addRecipientsToTemplateWhichIsNotFound(1L, RECIPIENT_LIST_ENTITY_JSON); // проверка сценария, когда шаблон,
        // к которому надо добавлять получателей, еще не существует.

        Long id = templateEntityCreation(TEMPLATE_ENTITY); // создание нового шаблона для добавления к нему получателей.
        getTemplateEntitySuccess(id, TEMPLATE_ENTITY); // проверка успешного получения шаблона.

        addRecipientsToTemplateWhichIsCreated(id, RECIPIENT_LIST_ENTITY_JSON, TEMPLATE_ENTITY); // добавление получателей к шаблону.
        getTemplateEntitySuccess(id, TEMPLATE_ENTITY); // проверяется, что получатели были успешно добавлены к шаблону.
    }

    /**
     * Метод для тестирования удаления получателей из шаблона.
     */
    @Test
    public void deleteRecipientsTest() throws Exception {
        deleteRecipientsFromTemplateWhichIsNotFound(1L, RECIPIENT_LIST_ENTITY_JSON); // проверка сценария, когда шаблон,
        // из которого надо удалять получателей, еще не существует.

        Long id = templateEntityCreation(TEMPLATE_ENTITY); // создание нового шаблона для удаления из него получателей.
        getTemplateEntitySuccess(id, TEMPLATE_ENTITY);

        addRecipientsToTemplateWhichIsCreated(id, new RecipientListEntityJson(List.of(1)), TEMPLATE_ENTITY); // добавление получателей к шаблону.
        deleteRecipientsFromTemplateSuccess(id, RECIPIENT_LIST_ENTITY_JSON, TEMPLATE_ENTITY); // удаление получателей из шаблона.
        getTemplateEntitySuccess(id, TEMPLATE_ENTITY); // проверяется, что получатели были успешно удалены из шаблона.
    }

    /**
     * Метод для тестового создания шаблона.
     *
     * @param templateEntityJson объект для создания шаблона сообщения.
     * @return возвращает идентификатор созданного шаблона.
     */
    private Long templateEntityCreation(TemplateEntityJson templateEntityJson) throws Exception {

        // отправка запроса на создание шаблона
        ResultActions result = mockMvc.perform(post(CREATE_TEMPLATE.getLinks())
                        .header("clientId", CLIENT_ID)
                        .content(templateEntityJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                // проверка успешного создания шаблона
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.templateTitle").value(templateEntityJson.templateTitle()),
                        jsonPath("$.templateContent").value(templateEntityJson.templateContent()),
                        jsonPath("$.templateImage").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
        return Long.valueOf(extractJsonValueByKey(result, "id"));
    }

    /**
     * Метод для тестирования конфликта при создании шаблона с уже существующим заголовком.
     *
     * @param templateEntityJson объект для создания шаблона сообщения.
     */
    private void templateEntityCreationConflict(TemplateEntityJson templateEntityJson) throws Exception {
        mockMvc.perform(post(CREATE_TEMPLATE.getLinks())
                        .header("clientId", CLIENT_ID)
                        .content(templateEntityJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.title_already_exists", templateEntityJson.templateTitle(), CLIENT_ID)
                        )
                );
    }

    /**
     * Метод для проверки поиска шаблона с указанным идентификатором(в результате проверки он должен быть не найден).
     *
     * @param templateId идентификатор шаблона.
     */
    private void getTemplateEntityNotFound(Long templateId) throws Exception {
        mockMvc.perform(get(GET_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll( // проверка, что запрашиваемый шаблон не найден.
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    /**
     * Метод для тестирования успешного получения шаблона с указанным идентификатором.
     *
     * @param templateId идентификатор шаблона.
     * @param templateEntityJson объект для создания шаблона сообщения.
     */
    private void getTemplateEntitySuccess(Long templateId, TemplateEntityJson templateEntityJson) throws Exception {

        mockMvc.perform(get(GET_TEMPLATE.getLinks().formatted(templateId)) // получение шаблона по идентификатору.
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(), // проверка его соответствия заданному шаблону.
                        jsonPath("$.id").exists(),
                        jsonPath("$.templateTitle").value(templateEntityJson.templateTitle()),
                        jsonPath("$.templateContent").value(templateEntityJson.templateContent()),
                        jsonPath("$.templateImage").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
    }

    /**
     * Метод для удаления шаблона по указанному идентификатору и проверки результат операции.
     *
     * @param templateId идентификатор шаблона.
     * @param result ожидаемый результат операции.
     */
    private void deleteTemplateEntityAndExpectResult(Long templateId, Boolean result) throws Exception {
        mockMvc.perform(delete(DELETE_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value(result)
                );
    }

    /**
     * Метод для проверки добавления получателей к несуществующему шаблону.
     *
     * @param templateId идентификатор шаблона.
     * @param recipientListEntityJson список получателей.
     */
    private void addRecipientsToTemplateWhichIsNotFound(Long templateId, RecipientListEntityJson recipientListEntityJson)
            throws Exception {

        // попытка добавить получателей к несуществующему шаблону
        mockMvc.perform(post(ADD_RECIPIENT.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipientListEntityJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                messageService.getMessage("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    /**
     * Метод для добавления получателей к существующему шаблону и проверки их успешного добавления.
     *
     * @param templateId идентификатор шаблона.
     * @param recipients список получателей.
     * @param templateEntityJson объект для создания шаблона сообщения.
     */
    private void addRecipientsToTemplateWhichIsCreated(Long templateId, RecipientListEntityJson recipients,
                                                       TemplateEntityJson templateEntityJson) throws Exception {
        mockMvc.perform(post(ADD_RECIPIENT.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.templateTitle").value(templateEntityJson.templateTitle()),
                        jsonPath("$.templateContent").value(templateEntityJson.templateContent()),
                        jsonPath("$.templateImage").isEmpty(),
                        jsonPath("$.recipientIds").isArray()
                );
    }

    /**
     * Метод для проверки удаления получателей из несуществующего шаблона.
     *
     * @param templateId  идентификатор шаблона.
     * @param recipients список получателей.
     */
    private void deleteRecipientsFromTemplateWhichIsNotFound(Long templateId, RecipientListEntityJson recipients) throws Exception {

        // попытка удалить получателей из несуществующего шаблона
        mockMvc.perform(delete(DELETE_TEMPLATE.getLinks().formatted(templateId))
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

    /**
     * Метод для удаления получателей из существующего шаблона и проверки их успешного удаления.
     *
     * @param templateId идентификатор шаблона.
     * @param recipients список получателей.
     * @param templateEntityJson объект для создания шаблона сообщения.
     */
    private void deleteRecipientsFromTemplateSuccess(Long templateId, RecipientListEntityJson recipients,
                                            TemplateEntityJson templateEntityJson) throws Exception {

        // удаление получателей из существующего шаблона.
        mockMvc.perform(delete(DELETE_TEMPLATE.getLinks().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.convertToJson())
                        .contentType(APPLICATION_JSON))
                // проверка на успешное удаление шаблона.
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.templateTitle").value(templateEntityJson.templateTitle()),
                        jsonPath("$.templateContent").value(templateEntityJson.templateContent()),
                        jsonPath("$.templateImage").isEmpty(),
                        jsonPath("$.recipientIds").isArray()
                );
    }

}
