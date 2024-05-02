package com.example.sender.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Интерфейс TelegramApiInterface представляет собой клиент для взаимодействия с Telegram Bot API с помощью Spring Cloud OpenFeign.
 * Он содержит методы для отправки запросов к API Telegram.
 */
@FeignClient(url = "https://api.telegram.org/", name = "TelegramAPI")
public interface TelegramApiInterface {

    /**
     * Метод отправляет GET-запрос на указанный URL для отправки сообщения через Telegram Bot API.
     *
     * @param chatId идентификатор чата или пользователя в Telegram, куда необходимо отправить сообщение.
     * @param messageText текст сообщения, которое нужно отправить.
     * @param apiKey ключ API бота Telegram для аутентификации запроса.
     * @return TelegramApiResponse ответ от API Telegram о статусе отправки сообщения.
     */
    @GetMapping(value = "/bot{apiKey}/sendMessage?chat_id={chatId}&text={messageText}")
    TelegramApiResponse sendMessageToUser(@PathVariable String chatId, @PathVariable String messageText, @PathVariable String apiKey);
}
