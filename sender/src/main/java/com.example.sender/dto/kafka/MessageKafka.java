package com.example.sender.dto.kafka;


import com.example.sender.dto.response.TemplateHistoryResponse;
import com.example.sender.model.NotificationChannel;
import com.example.sender.model.NotificationState;

import java.util.Map;

/**
 *
 */
public record MessageKafka(
        Long id, // Уникальный идентификатор уведомления
        NotificationChannel channel, // Тип уведомления
        String credential, // Учетные данные получателя уведомления.
        NotificationState state, //Текущий статус уведомления (например, ожидание, отправлено, ошибка).
        Integer retryAttempts, // Количество попыток повторной отправки уведомления в случае ошибки.
        TemplateHistoryResponse template, //Объект TemplateHistoryResponse, содержащий информацию о шаблоне уведомления
        Long clientId, // Идентификатор клиента, для которого предназначено уведомление.
        Map<String, String> urlOptionMap) //Карта (Map) с дополнительными параметрами или опциями, которые
        // могут быть использованы для генерации URLs в уведомлении.
{
}
