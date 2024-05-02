package com.example.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Класс представляет собой приложение сервера реестра Eureka для системы уведомлений.
 */
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryNotificationServerApplication {

    /**
     * Метод main, который запускает приложение сервера реестра Eureka.
     * @param args массив аргументов командной строки
     */
    public static void main(String[] args) {

        SpringApplication.run(DiscoveryNotificationServerApplication.class,args);
    }
}
