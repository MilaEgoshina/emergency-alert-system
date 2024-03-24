package com.example.security.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EndpointUrls {


    REGISTRATION_ENDPOINT("/api/v1/auth/register"),
    AUTHENTICATION_ENDPOINT("/api/v1/auth/authenticate"),
    VALIDATION_ENDPOINT("/api/v1/auth/validate"),
    LOGOUT_ENDPOINT("/api/v1/auth/logout");

    private final String endpointUrl;
}
