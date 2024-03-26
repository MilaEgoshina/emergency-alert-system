package com.example.bot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class BotForEmergencySystem extends TelegramLongPollingBot{


    private static final String INFO = "/info";

    @Value("${bot.name}")
    private String botCustomerName;

    public BotForEmergencySystem(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

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

    @Override
    public String getBotUsername() {
        return botCustomerName;
    }

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
