package com.example.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// позволяет приложению зарегистрироваться в системе обнаружения сервисов и находить через неё другие сервисы.
// implementations of DiscoveryClient auto-register the local Spring Boot server with the remote discovery server
@EnableDiscoveryClient
@SpringBootApplication
public class ApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServiceApplication.class, args);
    }
}
