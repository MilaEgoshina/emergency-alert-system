package com.example.security.builder;

/**
 * Класс для создания JSON-объектов, содержащих данные для аутентификации пользователя (электронную почту и пароль).
 */
public record CustomerCredentialsJson(

        String emailAddress,
        String passwordValue

) implements SecurityEntityTest{

    // статическая константа-шаблон, представляющая JSON-структуру для данных аутентификации.
    private static final String JSON_TEMPLATE = """ 
        {
            "emailAddress": %s,
            "passwordValue": %s
        }
        """;

    /**
     * Метод, который переопределяет метод convertToJson() из интерфейса SecurityEntityTest.
     *
     * @return возвращает JSON-представление данных аутентификации.
     */
    @Override
    public String convertToJson() {
        String formattedEmail = format(emailAddress);
        String formattedPassword = format(passwordValue);
        // подстановка обработанных значений emailVal и passwordVal в JSON-шаблон TEMPLATE.
        return JSON_TEMPLATE.formatted(formattedEmail, formattedPassword);
    }
}
