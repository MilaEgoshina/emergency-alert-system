package com.example.templ.integration;

import com.example.templ.builder.RecipientListEntityJson;
import com.example.templ.client.RecipientEntityClient;
import com.example.templ.config.LinksEnums;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AllArgsConstructor
public class KafkaListenerIntegrationTest extends IntegrationTestBase {

    @Value("${spring.kafka.topics.template-update}")
    private String topicName;

    private final KafkaTemplate<String, RecipientTemplateKafkaRecord> template;

    private final RecipientIdRepository recipientIdRepository;
    private final CustomKafkaListeners kafkaListeners;
    private final MockMvc mockMvc;

    @MockBean
    private final RecipientEntityClient recipientEntityClient;

    @BeforeEach
    void initialize() {
        RecipientEntityClientMock.setupRecipientEntityClientMock(recipientEntityClient);
    }

    @Test
    public void testConsumer() throws Exception {
        Long id = createTemplate();
        addRecipientsToTemplate(id);
        Thread.sleep(500L);

        testKafkaListener(Actions.PERSISTS, id, 1);
        testKafkaListener(Actions.REMOVE, id, 0);
    }

    private void testKafkaListener(Actions actions, Long id, Integer expectedSize) throws InterruptedException {
        RecipientTemplateKafkaRecord message = RecipientTemplateKafkaRecord.builder()
                .actions(actions)
                .templateId(id)
                .recipientId(1L)
                .build();

        CompletableFuture<Void> future = kafkaListeners.customListener(message);
        template.send(topicName, message);
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(future::isDone);
        Thread.sleep(500L);

        assertThat(recipientIdRepository.findAll()).hasSize(expectedSize);
    }

    private Long createTemplate() throws Exception {
        ResultActions result = mockMvc.perform(post(LinksEnums.CREATE_TEMPLATE.getLinks())
                .header("clientId", TemplateControllerIntegrationTest.CLIENT_ID)
                .content(TemplateControllerIntegrationTest.TEMPLATE.convertToJson())
                .contentType(APPLICATION_JSON));
        return Long.valueOf(extractJsonValueByKey(result, "id"));
    }

    private void addRecipientsToTemplate(Long id) throws Exception {
        mockMvc.perform(post(LinksEnums.ADD_RECIPIENT.getLinks().formatted(id))
                .header("clientId", TemplateControllerIntegrationTest.CLIENT_ID)
                .content(new RecipientListEntityJson(List.of(1)).convertToJson())
                .contentType(APPLICATION_JSON));
    }
}
