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
     * Этот метод вызывается после успешной отправки уведомления получателю, чтобы обновить статус уведомления на "отправлено".
     * @return ResponseEntity<NotificationResponse> ответ от удаленного API, содержащего объект NotificationResponse.
     */
    @PostMapping("/api/v1/notifications/{notificationId}/mark-sent")
    ResponseEntity<NotificationResponse> markNotificationAsSent(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );

    /**
     * Метод отправляет POST-запрос с идентификаторами клиента и уведомления в заголовках.
     * Этот метод вызывается, если произошла ошибка при отправке уведомления, и уведомление должно быть повторно отправлено.
     * @return ResponseEntity<NotificationResponse> ответ от удаленного API, содержащего объект NotificationResponse.
     */
    @PostMapping("/api/v1/notifications/{notificationId}/mark-resending")
    ResponseEntity<NotificationResponse> markNotificationAsResending(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );

    /**
     * Метод отправляет POST-запрос с идентификаторами клиента и уведомления в заголовках.
     * Этот метод вызывается в случае различных ошибок при обработке уведомления.
     * @return ResponseEntity<NotificationResponse> ответ от удаленного API, содержащего объект NotificationResponse.
     */
    @PostMapping("/api/v1/notifications/{notificationId}/mark-corrupt")
    ResponseEntity<NotificationResponse> markNotificationAsCorrupt(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );

    /**
     * Метод отправляет POST-запрос с идентификаторами клиента и уведомления в заголовках.
     * Этот метод вызывается в случае различных ошибок при обработке уведомления.
     * @return ResponseEntity<NotificationResponse> ответ от удаленного API, содержащего объект NotificationResponse.
     */
    @PostMapping("/api/v1/notifications/{notificationId}/mark-error")
    ResponseEntity<NotificationResponse> markNotificationAsError(
            @RequestHeader Long clientId,
            @PathVariable("notificationId") Long notificationId
    );
}
