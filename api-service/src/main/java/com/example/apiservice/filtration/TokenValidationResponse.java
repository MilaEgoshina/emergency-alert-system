package com.example.apiservice.filtration;

/**
 * Класс TokenValidationResponse представляет объект ответа на проверку токена.
 */
public class TokenValidationResponse {

    private String clientId;
    private boolean isValid;

    /**
     * Метод для получения идентификатора клиента.
     * @return Идентификатор клиента
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Метод для проверки валидности токена.
     * @return true, если токен валиден, false в противном случае
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Метод для установки идентификатора клиента.
     * @param clientId Идентификатор клиента
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Метод для установки флага валидности токена.
     * @param valid Флаг валидности токена
     */
    public void setValid(boolean valid) {
        isValid = valid;
    }
}
