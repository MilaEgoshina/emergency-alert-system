package com.example.config;

import com.example.bot.BotForEmergencySystem;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс представляет собой конфигурацию для системы аварийного уведомления.
 */
@Configuration
public class ConfigForEmergencySystem {

    /**
     * Метод для получения экземпляра TelegramBotsApi и регистрации бота для системы аварийного уведомления.
     *
     * @param emergencyBot экземпляр бота для системы аварийного уведомления
     * @return экземпляр TelegramBotsApi для работы с Telegram ботом
     * @throws TelegramApiException в случае возникновения ошибки при регистрации бота
     */
    public TelegramBotsApi getTelegramBotApi(BotForEmergencySystem emergencyBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(emergencyBot);
        return telegramBotsApi;
    }

}
