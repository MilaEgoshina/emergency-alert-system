package com.example.security.builder;


/**
 * Класс используется для создания объектов типа CustomerCredentialsJson.
 */
public class CustomerCredentialsJsonConstructor extends SecurityEntityBuilderTest<CustomerCredentialsJson>{

    // значения электронной почты и пароля, которые будут использоваться для создания экземпляра CustomerCredentialsJson.
    private String emailAddress;
    private String passwordValue;

    public static CustomerCredentialsJsonConstructor getConstructor() {
        return new CustomerCredentialsJsonConstructor();
    }

    public CustomerCredentialsJsonConstructor setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public CustomerCredentialsJsonConstructor setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
        return this;
    }


    @Override
    public CustomerCredentialsJson build() {
        return new CustomerCredentialsJson(emailAddress, passwordValue);
    }
}
