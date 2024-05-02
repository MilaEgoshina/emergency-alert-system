package com.example.rebalancer.service;

import com.example.rebalancer.dto.kafka.NotificationMessageKafka;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * Интерфейс, который описывает контракт для вызова удаленного сервиса, отвечающего за получение уведомлений
 * для перебалансировки (rebalancing).
 */
@FeignClient("${services.message}")
public interface NotificationService {

    /**
     * Метод для получения уведомлений для перебалансировки с заданными параметрами.
     * @param pendingThreshold время в секундах, определяющее, какие уведомления считать "ожидающими"
     * @param newThreshold время в секундах, определяющее, какие уведомления считать "новыми"
     * @param limit максимальное количество уведомлений, которое будет возвращено (по умолчанию 20).
     * @return возвращает ResponseEntity<List<NotificationMessageKafka>>, что означает, что ожидается получить HTTP-ответ,
     * содержащий список объектов NotificationMessageKafka.
     */
    @GetMapping("/api/v1/messages/")
    ResponseEntity<List<NotificationMessageKafka>> fetchNotificationsForRebalancing(
            @RequestParam(name = "pending", required = false, defaultValue = "10") Long pendingThreshold,
            @RequestParam(name = "new", required = false, defaultValue = "10") Long newThreshold,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit
    );
}
