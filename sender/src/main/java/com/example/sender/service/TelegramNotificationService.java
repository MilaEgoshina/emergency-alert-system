package com.example.sender.service;

import com.example.sender.dto.response.TemplateHistoryResponse;
import feign.FeignException.BadRequest;
import feign.FeignException.Forbidden;
import feign.FeignException.TooManyRequests;
import feign.RetryableException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Класс для отправки уведомлений в Telegram.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotificationService {

    @Value("${message.services.telegram.api.key}")
    private String apiKey;

    private final TelegramApiInterface telegramApiClient;

    /**
     * Метод для отправки сообщений
     * @param telegramUserId ID пользователя Telegram
     * @param templateMessage объект TemplateHistoryResponse, который содержит заголовок, содержимое и URL изображения для уведомления.
     * @return true в случае успешной отправки сообщения
     */
    public boolean sendNotification(String telegramUserId,  TemplateHistoryResponse templateMessage) {
        String imageUrl = Optional.ofNullable(templateMessage.getImageUrl()).orElse("");
        String content = Optional.ofNullable(templateMessage.getContent()).orElse("");
        String message = "%s\n\n%s\n\n%s".formatted(templateMessage.getTitle(), content, imageUrl);

        return sendMessageToUser(telegramUserId, message);
    }

    /**
     * Вспомогательный метод для отправки сообщений в Telegram.
     *
     * @param telegramUserId ID пользователя Telegram.
     * @param message текст сообщения.
     * @return true, если сообщение успешно отправлено, в противном случае - false.
     */
    private boolean sendMessageToUser(String telegramUserId, String message) {
        try {
            TelegramApiResponse response = telegramApiClient.sendMessageToUser(telegramUserId, message, apiKey);
            return response.success();
        } catch (BadRequest | Forbidden e) {
            // Возвращает false, что означает, что сообщение не было отправлено
            return false;
        } catch (TooManyRequests | RetryableException e) {
            log.warn("Too many requests to Telegram API, retrying in 10 seconds");
            //Ждет 10 секунд, а затем повторяет попытку отправки сообщения с помощью рекурсивного вызова метода send.
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return sendMessageToUser(telegramUserId, message);
        }
    }
}