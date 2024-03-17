package com.example.sender.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Класс - интерфейс, созданный с помощью Spring Cloud OpenFeign для взаимодействия с Telegram Bot API.
 */
@FeignClient(url = "https://api.telegram.org/", name = "TelegramAPI")
public interface TelegramApiInterface {

    /**
     * Метод отправляет GET-запрос на URL
     * @return TelegramApiResponse ответ об успешном подключении или же нет к Telegram API
     */
    @GetMapping(value = "/bot{apiKey}/sendMessage?chat_id={chatId}&text={messageText}")
    TelegramApiResponse sendMessageToUser(@PathVariable String chatId, @PathVariable String messageText, @PathVariable String apiKey);
}
