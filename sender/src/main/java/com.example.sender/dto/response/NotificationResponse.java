package com.example.sender.dto.response;

import com.example.sender.model.NotificationChannel;
import com.example.sender.model.NotificationState;

import java.time.LocalDateTime;

/**
 * Record-класс, который представляет ответ с информацией об уведомлении.
 */
public record NotificationResponse(
        Long id, // идентификатор уведомления
        NotificationChannel channel, // тип уведомления, значение из NotificationChannel
        String credential, // учетные данные для отправки уведомления
        NotificationState state, // статус уведомления, значение из NotificationState
        Integer retryAttempts, // количество попыток переотправки
        LocalDateTime createdAt, // время создания уведомления
        TemplateHistoryResponse template, // информация о шаблоне уведомления, объект TemplateHistoryResponse
        Long clientId) // идентификатор клиента
{

}
