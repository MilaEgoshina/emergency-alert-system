package com.example.recipient.entity;

import com.example.recipient.listener.TemplateEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс - сущность JPA, который используется для моделирования отношения "многие ко многим" между сущностями
 * RecipientEntity и TemplateEntity в базе данных.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Указываем имя таблицы и определяем уникальное ограничение, гарантирующее, что комбинация
// recipient_id и template_id является уникальной.
@Table(
        name = "template_entities",
        uniqueConstraints = {
                @UniqueConstraint(name = "template_entities_unq_recipient-trample", columnNames = {"recipient_Id", "templateId"})
        }
)
// Указываем прослушиватель сущности, который будет вызываться перед сохранением экземпляра TemplateEntity в базе данных.
@EntityListeners(TemplateEntityListener.class)
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Идентификатор сущности, сгенерированный базой данных.
    private Long id;

    @ManyToOne
    // Экземпляр сущности RecipientEntity, представляющий получателя, которому принадлежит шаблон.
    private RecipientEntity recipientEntity;

    // Идентификатор шаблона, связанного с получателем.
    private Long templateId;
}
