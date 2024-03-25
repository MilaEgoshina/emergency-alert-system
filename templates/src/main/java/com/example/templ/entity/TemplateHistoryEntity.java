package com.example.templ.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Класс - сущность истории шаблонов, где каждая запись содержит информацию о шаблоне, включая его заголовок,
 * содержимое, URL изображения, а также связанные идентификаторы клиента и ответа.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template_history")
public class TemplateHistoryEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // идентификатор записи в таблице, является первичным ключом.

    private Long clientId; // идентификатор клиента, связанного с этим шаблоном.
    private Long responseId; // идентификатор ответа, связанного с этим шаблоном.

    @Column(nullable = false)
    private String header; // заголовок шаблона, не может быть null.

    private String details; // содержимое самого шаблона.

    private String linkURL; // URL-адрес изображения, связанного с этим шаблоном.

    /**
     * Метод для установки значение поля clientId
     *
     * @param clientId идентификатор клиента, который необходимо установить.
     * @return возвращает обновленный объект TemplateHistory.
     */
    public TemplateHistoryEntity assignClient(Long clientId) {
        setClientId(clientId);
        return this;
    }
}
