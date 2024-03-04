package com.example.apiservice.filtration;


public class TokenValidationResponse {

    private String clientId;
    private boolean isValid;

    public String getClientId() {
        return clientId;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
