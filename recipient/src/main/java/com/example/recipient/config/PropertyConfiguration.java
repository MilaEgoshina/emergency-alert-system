package com.example.recipient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:${envTarget:error}.properties"
})

public class PropertyConfiguration {
}
