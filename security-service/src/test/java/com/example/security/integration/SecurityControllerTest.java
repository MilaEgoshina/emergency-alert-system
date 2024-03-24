package com.example.security.integration;

import com.example.security.builder.CustomerCredentialsJson;
import com.example.security.builder.CustomerCredentialsJsonConstructor;
import com.example.security.service.CustomMessageSourceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.security.configuration.EndpointUrls.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class SecurityControllerTest extends BaseIntegrationTest{

    private final MockMvc mockMvc;

    private final CustomMessageSourceService messageSourceService;

    static final CustomerCredentialsJson CUSTOMER = CustomerCredentialsJsonConstructor.getConstructor()
            .setEmailAddress("eva@gmail.com")
            .setPasswordValue("123456")
            .build();

    static final CustomerCredentialsJson EXISTING_CUSTOMER = CustomerCredentialsJsonConstructor.getConstructor()
            .setEmailAddress("customer@gamil.com")
            .setPasswordValue("123456")
            .build();

    @Test
    public void testRegistration() throws Exception {
        registerCustomerReturnTrue(CUSTOMER);
        registerCustomerEmailAlreadyExists(CUSTOMER);
        registerCustomerEmailAlreadyExists(EXISTING_CUSTOMER);
    }

    @Test
    public void testAuthenticationSuccess() throws Exception {
        registerCustomerReturnTrue(CUSTOMER);
        String jwt = authenticationReturnToken(CUSTOMER);

        assertThat(jwt).isNotNull().isNotBlank();
    }

    @Test
    public void testAuthenticationFailure() throws Exception {
        authenticationNotFound(CUSTOMER);
        registerCustomerReturnTrue(CUSTOMER);

        CustomerCredentialsJson invalidCredentialCUSTOMER = CustomerCredentialsJsonConstructor.getConstructor()
                .setEmailAddress(CUSTOMER.emailAddress())
                .setPasswordValue(CUSTOMER.passwordValue() + "!")
                .build();
        authenticationBadCredentials(invalidCredentialCUSTOMER);

        CustomerCredentialsJson invalidValuesJson = CustomerCredentialsJsonConstructor.getConstructor()
                .setEmailAddress("@")
                .build();
        registerInvalidRequest(invalidValuesJson);
    }

    @Test
    public void testValidationSuccess() throws Exception {
        registerCustomerReturnTrue(CUSTOMER);
        String token = authenticationReturnToken(CUSTOMER);
        validationSuccess(token);
    }

    @Test
    public void testValidationFailure() throws Exception {
        String dummyToken = "dummy";
        validationForbidden(dummyToken);
    }

    @Test
    public void testLogout() throws Exception {
        registerCustomerReturnTrue(CUSTOMER);
        String token = authenticationReturnToken(CUSTOMER);
        validationSuccess(token);
        logout(token);
        validationInvalidToken(token);
    }

    private void registerCustomerReturnTrue(CustomerCredentialsJson CustomerCredentialsJson) throws Exception {
        mockMvc.perform(post(REGISTRATION_ENDPOINT.getEndpointUrl())
                        .content(CustomerCredentialsJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value("true")
                );
    }

    private void registerInvalidRequest(CustomerCredentialsJson CustomerCredentialsJson) throws Exception {
        mockMvc.perform(post(REGISTRATION_ENDPOINT.getEndpointUrl())
                        .content(CustomerCredentialsJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(not(emptyString()))
                );
    }

    private void registerCustomerEmailAlreadyExists(CustomerCredentialsJson CustomerCredentialsJson) throws Exception {
        mockMvc.perform(post(REGISTRATION_ENDPOINT.getEndpointUrl())
                        .content(CustomerCredentialsJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.message").value(messageSourceService.getMessage("customer.email.already_exists"))
                );
    }

    private String authenticationReturnToken(CustomerCredentialsJson CustomerCredentialsJson) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(AUTHENTICATION_ENDPOINT.getEndpointUrl())
                        .content(CustomerCredentialsJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.jwt").isString()
                );

        return extractJsonValueByKey(resultActions, "token");
    }

    private void authenticationBadCredentials(CustomerCredentialsJson CustomerCredentialsJson) throws Exception {
        mockMvc.perform(post(AUTHENTICATION_ENDPOINT.getEndpointUrl())
                        .content(CustomerCredentialsJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value(messageSourceService.getMessage("customer.bad_cred"))
                );
    }

    private void authenticationNotFound(CustomerCredentialsJson CustomerCredentialsJson) throws Exception {
        mockMvc.perform(post(AUTHENTICATION_ENDPOINT.getEndpointUrl())
                        .content(CustomerCredentialsJson.convertToJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(messageSourceService.getMessage("customer.not_found", CustomerCredentialsJson.emailAddress()))
                );
    }

    private void validationSuccess(String token) throws Exception {
        mockMvc.perform(get(VALIDATION_ENDPOINT.getEndpointUrl())
                        .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isOk(),
                        content().string(not(emptyString()))
                );
    }

    private void validationForbidden(String token) throws Exception {
        mockMvc.perform(get(VALIDATION_ENDPOINT.getEndpointUrl())
                        .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isForbidden()
                );
    }

    private void validationInvalidToken(String token) throws Exception {
        mockMvc.perform(get(VALIDATION_ENDPOINT.getEndpointUrl())
                        .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.message").value(messageSourceService.getMessage("token.invalid"))
                );
    }

    private void logout(String token) throws Exception {
        mockMvc.perform(get(LOGOUT_ENDPOINT.getEndpointUrl())
                        .header(AUTHORIZATION, "Bearer " + token))
                .andExpectAll(
                        status().isOk()
                );
    }
}
