package com.example.sender.service;

/**
 * Класс - record, который представляет собой ответ от Telegram API и указывает, был ли запрос к API успешным или нет.
 */
public record TelegramApiResponse(boolean success) {
}
