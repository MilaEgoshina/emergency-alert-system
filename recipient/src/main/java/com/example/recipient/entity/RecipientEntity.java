package com.example.recipient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/*@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "Recipients",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueClientAndEmail", columnNames = {"clientId", "email"}),
                @UniqueConstraint(name = "UniqueClientAndTelegram", columnNames = {"clientId", "telegramId"}),
                @UniqueConstraint(name = "UniqueClientAndPhoneNumber", columnNames = {"clientId", "phoneNumber"})
        },
        indexes = {
                @Index(name = "IndexEmail", columnList = "email")
        }
)
public class RecipientEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    private String name;
    private LocationData locationData;

    @Column(nullable = false)
    private String email;
    private String telegramId;
    private String phoneNumber;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(
            mappedBy = "recipient",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TemplateIdEntity> templateIds = new ArrayList<>();

    public RecipientEntity linkToClient(Long clientId) {
        setClientId(clientId);
        return this;
    }

    public void registerTemplate(Long templateId) {
        templateIds.add(
                TemplateEntityId.builder()
                        .templateId(templateId)
                        .recipient(this)
                        .build()
        );
    }

    public RecipientEntity deregisterTemplate(Long templateId) {
        templateIds.removeIf(
                template -> template.getTemplateId().equals(templateId)
        );
        return this;
    }
}*/
