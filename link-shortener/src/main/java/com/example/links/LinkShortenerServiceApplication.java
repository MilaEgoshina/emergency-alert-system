package com.example.links;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LinkShortenerServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(LinkShortenerServiceApplication.class,args);

    }
}
