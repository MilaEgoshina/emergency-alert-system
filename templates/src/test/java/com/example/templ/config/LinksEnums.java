package com.example.templ.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс - набор констант, представляющих различные URL-адреса (конечные точки API).
 */
@Getter
@AllArgsConstructor
public enum LinksEnums {

    CREATE_TEMPLATE("/api/v1/templates/"), // URL-адрес для создания нового шаблона.
    GET_TEMPLATE("/api/v1/templates/%s"), // URL-адрес для получения существующего шаблона по его идентификатору (%s - заполнитель для идентификатора).
    DELETE_TEMPLATE("/api/v1/templates/%s"), // URL-адрес для удаления существующего шаблона по его идентификатору.
    ADD_RECIPIENT("/api/v1/templates/%s/recipients"), // URL-адрес для добавления получателя к существующему шаблону.
    DELETE_RECIPIENT("/api/v1/templates/%s/recipients"), // URL-адрес для удаления получателя из существующего шаблона.
    CREATE_HISTORY("/api/v1/templates/history/%s"), // URL-адрес для создания истории для существующего шаблона.
    GET_HISTORY("/api/v1/templates/history/%s"); // URL-адрес для получения истории существующего шаблона.

    private final String links; // строковое значение, представляющее соответствующий URL-адрес.

}
