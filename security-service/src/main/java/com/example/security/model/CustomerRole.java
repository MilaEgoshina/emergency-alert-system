package com.example.security.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Класс - enum, который определяет различные роли пользователей и их прав доступа в системе.
 */
public enum CustomerRole implements GrantedAuthority {

    CUSTOMER,
    MANAGER;

    /**
     * Метод для определения прав доступа пользователей в системе.
     * @return возвращает строковое представление авторитета (права доступа).
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
