package com.example.recipient.entity;

import com.example.recipient.listener.TemplateEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Eventual Consistency | ManyToMany with template-service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "template_ids",
        uniqueConstraints = {
                @UniqueConstraint(name = "template_ids_unq_recipient-trample", columnNames = {"recipient_Id", "templateId"})
        }
)
@EntityListeners(TemplateEntityListener.class)
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RecipientEntity recipientEntity;

    private Long templateId;
}
