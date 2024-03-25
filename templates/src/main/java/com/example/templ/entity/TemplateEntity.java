package com.example.templ.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "templates",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_clientId_title", columnNames = {"clientId", "templateTitle"}),
        }
)
public class TemplateEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //идентификатор шаблона

    private Long clientId; // идентификатор клиента, к которому относится данный шаблон.
    private Long responseId; // Идентификатор ответа, связанного с этим шаблоном.

    @Column(nullable = false)
    private String templateTitle; // Заголовок шаблона. Является обязательным полем (nullable = false).
    private String templateContent; // Содержимое шаблона, текстовое поле, содержащее текст шаблона.
    private String templateImage; //  Изображение, связанное с шаблоном.

    @ToString.Exclude
    @Builder.Default
    @OneToMany(
            mappedBy = "templateEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecipientId> recipients = new ArrayList<>(); // Список получателей данного шаблона, представлен в виде списка объектов RecipientId

    public TemplateEntity setClient(Long clientId) {
        setClientId(clientId);
        return this;
    }

    public TemplateEntity setResponse(Long responseId) {
        setResponseId(responseId);
        return this;
    }

    public void addRecipient(Long recipientId) {
        recipients.add(
                RecipientId.builder()
                        .recipientId(recipientId)
                        .templateEntity(this)
                        .build()
        );
    }

    public TemplateEntity removeRecipient(Long recipientId) {
        recipients.removeIf(
                recipient -> Objects.equals(recipient.getRecipientId(), recipientId)
        );
        return this;
    }
}

