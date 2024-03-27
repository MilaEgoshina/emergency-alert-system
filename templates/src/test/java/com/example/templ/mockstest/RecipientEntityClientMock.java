package com.example.templ.mockstest;

import com.example.templ.client.RecipientEntityClient;
import com.example.templ.dto.response.RecipientEntityResponse;
import feign.FeignException;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class RecipientEntityClientMock {


    public static void setupRecipientEntityClientMock(RecipientEntityClient recipientEntityClient) {
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

    private static void createWhenFetchEntityById(RecipientEntityClient recipientEntityClient, Long id) {
        when(
                recipientEntityClient.getRecipientById(any(), eq(id))
        ).thenReturn(
                ResponseEntity.ofNullable(RecipientEntityResponse.builder().id(id).build())
        );
    }

}
