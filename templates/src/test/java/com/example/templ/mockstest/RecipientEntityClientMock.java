package com.example.templ.mockstest;

import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.response.RecipientEntityResponse;
import feign.FeignException;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Класс для создания макета (mock) клиента `RecipientEntityClient`.
 */
public class RecipientEntityClientMock {

    /**
     * Метод для настройки поведения макета `RecipientEntityClient`.
     *
     * @param recipientEntityClient объект класса RecipientEntityClient, для которого будет эмулироваться поведение
     * реального клиента при вызове метода.
     */
    public static void setupRecipientEntityClientMock(RecipientEntityClient recipientEntityClient) {

        // при вызове метода `getRecipientResponseListByTemplateId` с любыми аргументами,
        // будет возвращаться пустой список `Collections.emptyList()`
        when(
                recipientEntityClient.getRecipientResponseListByTemplateId(any(), any())
        ).thenReturn(
                ResponseEntity.ofNullable(Collections.emptyList())
        );

        createWhenFetchEntityById(recipientEntityClient, 1L);
        createWhenFetchEntityById(recipientEntityClient, 2L);

        when(
                recipientEntityClient.getRecipientById(any(), eq(3L))
        ).thenThrow(FeignException.NotFound.class);
    }

    /**
     * Метод для настройки макета `RecipientEntityClient` с конкретным идентификатором.
     *
     * @param recipientEntityClient объект класса RecipientEntityClient, для которого будет эмулироваться поведение
     * реального клиента при вызове метода.
     * @param id конкретный идентификатор клиента, для которого необходимо эмулировать поведение.
     */
    private static void createWhenFetchEntityById(RecipientEntityClient recipientEntityClient, Long id) {

        // при вызове метода `getRecipientById` с определенным идентификатором `id`,
        // будет возвращаться объект `RecipientEntityResponse` с установленным идентификатором.

        when(
                recipientEntityClient.getRecipientById(any(), eq(id))
        ).thenReturn(
                ResponseEntity.ofNullable(RecipientEntityResponse.builder().id(id).build())
        );
    }

}
