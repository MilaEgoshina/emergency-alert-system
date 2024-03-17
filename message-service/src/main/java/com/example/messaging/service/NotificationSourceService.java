package com.example.messaging.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

/**
 * Класс, который обеспечивает локализацию текстовых сообщений из внешнего файла ресурсов.
 */
@Service
@RequiredArgsConstructor
public class NotificationSourceService {

    // используется для получения локализованных сообщений из файла ресурсов, называемого messages.properties.
    private final ResourceBundle bundle;

    public NotificationSourceService() {

        // загружаем сообщения из файла messages.properties
        bundle = ResourceBundle.getBundle("messages.properties");
    }

    /**
     * Метод, который использует ResourceBundle при поиске ключа key возвращает соответствующее сообщение.
     * @param key ключ для поиска необходимо сообщения из файла ресурса
     * @return возвращает соответствующее сообщение
     */
    public String getMessage(String key) {
        return bundle.getString(key);
    }


    /**
     * Метод позволяет передавать параметры формата для замены плейсхолдеров в сообщении.
     * @param key ключ для поиска необходимо сообщения из файла ресурса
     * @param params параметры, в соответствие с которыми форматируется сообщение
     * @return возвращает соответствующее сообщение
     */
    public String getMessage(String key, Object... params) {
        String message = bundle.getString(key);
        return String.format(message, params);
    }
}
