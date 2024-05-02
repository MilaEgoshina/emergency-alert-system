package com.example.bot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс представляет собой Telegram-бота для системы аварийного уведомления.
 */
@Slf4j
@Component
public class BotForEmergencySystem extends TelegramLongPollingBot{


    private static final String INFO = "/info";

    @Value("${bot.name}")
    private String botCustomerName;

    /**
     * Конструктор класса. Инициализирует бота с указанным токеном.
     * @param botToken токен бота
     */
    public BotForEmergencySystem(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    /**
     * Метод, обрабатывающий входящие обновления от пользователей.
     * @param update объект обновления
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equals(INFO)) {
            sendNotification(chatId, "Идентификатор для регистрации в системе уведомлений: " + chatId);
        }
    }

    /**
     * Метод для получения имени бота.
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return botCustomerName;
    }

    /**
     * Метод для отправки уведомления пользователю.
     * @param chatId идентификатор чата
     * @param content содержание уведомления
     */
    private void sendNotification(Long chatId, String content) {
        String getChatId = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(getChatId, content);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка {}: {}", chatId, e.getMessage());
        }
    }
}
