package com.example.messaging.use;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, который отслеживает количество экземпляров сервисов, зарегистрированных в сервисном реестре поставщика Discovery Client.
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ServiceInstanceTracker {

    // используется для получения информации о зарегистрированных сервисах и их экземплярах.
    private final DiscoveryClient discoveryClient;
    private final Map<String, Integer> serviceInstanceCounts = new HashMap<>(); // для хранения количества экземпляров каждого сервиса.

    /**
     * Метод, который подсчитывает количество запущенных экземпляров конкретного сервиса по его имени.
     * @param serviceName имя сервиса
     * @return возвращает количество экземпляров конкретного сервиса.
     */
    public Integer getRunningInstanceCount(String serviceName) {
        serviceInstanceCounts.putIfAbsent(serviceName, retrieveInstanceCount(serviceName));
        return serviceInstanceCounts.get(serviceName);
    }

    /**
     * Метод обновляет информацию о количестве экземпляров всех сервисов, если она уже была получена.
     */
    @Scheduled(fixedDelay = 5000)
    private void monitorServiceInstances() {
        if (!serviceInstanceCounts.isEmpty()) {
            serviceInstanceCounts.replaceAll((serviceName, dummy) -> retrieveInstanceCount(serviceName));
        }
    }

    /**
     * Метод получает количество экземпляров конкретного сервиса, используя DiscoveryClient.
     * @param serviceName имя сервиса
     * @return возвращает количество экземпляров конкретного сервиса.
     */
    private Integer retrieveInstanceCount(String serviceName) {
        return discoveryClient.getInstances(serviceName).size();
    }
}
