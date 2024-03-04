package com.example.apiservice.filtration;


public class TokenValidationResponse {

    private Long clientId;
    private boolean isValid;

    public Long getClientId() {
        return clientId;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
