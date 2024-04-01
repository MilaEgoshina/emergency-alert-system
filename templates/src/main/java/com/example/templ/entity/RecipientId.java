package com.example.templ.entity;

import com.example.templ.listener.RecipientIdListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс - сущность для установки отношений между шаблонами (TemplateEntity) и их получателями.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "recipient_ids",
        uniqueConstraints = {
                @UniqueConstraint(name = "recipient_ids_unq_template-recipient", columnNames = {"template_id", "recipientId"})
        }
)
@EntityListeners(RecipientIdListener.class)
public class RecipientId implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TemplateEntity templateEntity; // каждый объект RecipientId связан с одним объектом TemplateEntity.

    private Long recipientId; // идентификатор получателя, который связан с конкретным шаблоном.
}
