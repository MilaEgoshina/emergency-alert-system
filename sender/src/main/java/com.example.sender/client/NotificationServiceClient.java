package com.example.sender.client;
import com.example.sender.dto.response.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Интерфейс, который определяет методы для взаимодействия с удаленным REST API сервисом, отвечающим за уведомления.
 */
@FeignClient(name = "${services.notification-service}")
public interface NotificationServiceClient {

    /**
     * Метод отправляет POST-запрос с идентификаторами клиента и уведомления в заголовках.
     * @return ResponseEntity<NotificationResponse> ответ от удаленного API, содержащего объект NotificationResponse.
     */
    @PostMapping("/api/v1/notifications/{notificationId}/mark-sent")
    ResponseEntity<NotificationResponse> markNotificationAsSent(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );

    /**
     *
     */
    @PostMapping("/api/v1/notifications/{notificationId}/mark-resending")
    ResponseEntity<NotificationResponse> markNotificationAsResending(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );

    @PostMapping("/api/v1/notifications/{notificationId}/mark-corrupt")
    ResponseEntity<NotificationResponse> markNotificationAsCorrupt(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );

    @PostMapping("/api/v1/notifications/{notificationId}/mark-error")
    ResponseEntity<NotificationResponse> markNotificationAsError(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );
}
