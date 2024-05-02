package com.example.messaging.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс CustomPropertiesConfig представляет конфигурацию для работы с пользовательскими свойствами.
 */
@Configuration
@PropertySource({
        "classpath:${envTarget:errors}.properties"
})
public class CustomPropertiesConfig {

}
