package com.example.config;

import com.example.bot.BotForEmergencySystem;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ConfigForEmergencySystem {

    public TelegramBotsApi getTelegramBotApi(BotForEmergencySystem emergencyBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(emergencyBot);
        return telegramBotsApi;
    }

}
