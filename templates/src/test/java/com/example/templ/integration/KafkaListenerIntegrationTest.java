package com.example.templ.integration;

import com.example.templ.builder.RecipientListEntityJson;
import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.kafka.Actions;
import com.example.templ.dto.kafka.RecipientTemplateKafkaRecord;
import com.example.templ.listener.CustomKafkaListeners;
import com.example.templ.mockstest.RecipientEntityClientMock;
import com.example.templ.repository.RecipientIdRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.templ.config.LinksEnums.ADD_RECIPIENT;
import static com.example.templ.config.LinksEnums.CREATE_TEMPLATE;
import static com.example.templ.integration.TemplateControllerIntegrationTest.CLIENT_ID;
import static com.example.templ.integration.TemplateControllerIntegrationTest.TEMPLATE_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Класс для интеграционного тестирования, который проверяет работу Kafka слушателя в приложении.
 */
@AllArgsConstructor
public class KafkaListenerIntegrationTest extends IntegrationTestBase {

    @Value("${spring.kafka.topics.template-update}")
    private String topicName;

    private final KafkaTemplate<String, RecipientTemplateKafkaRecord> template; // шаблон Kafka для отправки сообщений

    private final RecipientIdRepository recipientIdRepository; // репозиторий для хранения ID получателей
    private final CustomKafkaListeners kafkaListeners; // слушатель Kafka
    private final MockMvc mockMvc; // для выполнения HTTP запросов в тестах

    @MockBean
    private final RecipientEntityClient recipientEntityClient; // мок-бин для взаимодействия с клиентом получателя.

    @BeforeEach
    void initialize() {

        //  устанавливается мок-бин клиента.
        RecipientEntityClientMock.setupRecipientEntityClientMock(recipientEntityClient);
    }

    @Test
    public void testConsumer() throws Exception {
        Long id = createTemplateEntity(); // создание шаблона.
        addRecipientsToTemplate(id); // добавление пользователей к созданному шаблону.
        Thread.sleep(500L);

        // запуск Kafka слушателя и проверка его работы с разными операциями над шаблонами и получателями.
        testKafkaListener(Actions.PERSISTS, id, 1);
        testKafkaListener(Actions.REMOVE, id, 0);
    }

    /**
     *  Метод для отправки сообщения в Kafka и ожидания его обработки слушателем.
     *
     * @param actions действие (добавление или удаление), которое должно быть отправлено и обработано слушателем.
     * @param id идентификатор шаблона, для которого будет выполняться тест.
     * @param expectedSize ожидаемое количество получателей в репозитории после выполнения операции.
     * @throws InterruptedException выбрасывание исключения, так как внутри метода применяется вызов Thread.sleep(500L),
     * который может выбросить это исключение.
     */
    private void testKafkaListener(Actions actions, Long id, Integer expectedSize) throws InterruptedException {

        RecipientTemplateKafkaRecord message = RecipientTemplateKafkaRecord.builder()
                .actions(actions)
                .templateId(id)
                .recipientId(1L)
                .build();

        CompletableFuture<Void> future = kafkaListeners.customListener(message); // запуск слушателя Kafka.
        template.send(topicName, message); // отправление сообщение в топик Kafka.
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(future::isDone);
        Thread.sleep(500L);

        // проверка ожидаемого количества получателей в репозитории
        assertThat(recipientIdRepository.findAll()).hasSize(expectedSize);
    }

    private Long createTemplateEntity() throws Exception {
        ResultActions result = mockMvc.perform(post(CREATE_TEMPLATE.getLinks())
                .header("clientId", CLIENT_ID)
                .content(TEMPLATE_ENTITY.convertToJson())
                .contentType(APPLICATION_JSON));
        return Long.valueOf(extractJsonValueByKey(result, "id"));
    }

    private void addRecipientsToTemplate(Long id) throws Exception {
        mockMvc.perform(post(ADD_RECIPIENT.getLinks().formatted(id))
                .header("clientId", CLIENT_ID)
                .content(new RecipientListEntityJson(List.of(1)).convertToJson())
                .contentType(APPLICATION_JSON));
    }
}
