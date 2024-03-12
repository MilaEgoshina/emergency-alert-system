package com.example.recipient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Класс представляет сущность "Recipient", которая используется для хранения информации о получателях сообщений.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "recipients",
        uniqueConstraints = {
                @UniqueConstraint(name = "recipients_unq_clientId-email", columnNames = {"clientId", "email"}),
                @UniqueConstraint(name = "recipients_unq_clientId-telegramId", columnNames = {"clientId", "telegramId"}),
                @UniqueConstraint(name = "recipients_unq_clientId-phoneNumber", columnNames = {"clientId", "phoneNumber"})
        },
        indexes = {
                // Создаем индекс по столбцу email таблицы recipients для хранения структуры данных,
                // которая позволяет быстро находить записи по значению поля email
                @Index(name = "recipients_idx_email", columnList = "email")
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

    // Исключаем список TemplateId из методов toString()
    @ToString.Exclude
    @Builder.Default
    //одному экземпляру класса RecipientEntity может соответствовать несколько экземпляров класса TemplateEntity.
    @OneToMany(
            // указывает на поле recipientEntity в классе TemplateEntity, которое ссылается на данный экземпляр класса RecipientEntity.
            mappedBy = "recipientEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TemplateEntity> templateEntityIds = new ArrayList<>();

    /**
     * Метод добавляет идентификатор клиента к текущему экземпляру получателя.
     * @param clientId идентификатор клиента
     * @return возвращает текущий экземпляр RecipientEntity
     */
    public RecipientEntity addClientId(Long clientId) {
        setClientId(clientId);
        return this;
    }

    /**
     * Метод добавляет идентификатор шаблона в список templateEntityIds текущего экземпляра RecipientEntity.
     * @param templateId идентификатор шаблона
     */
    public void addTemplateEntity(Long templateId) {
        templateEntityIds.add(
                TemplateEntity.builder()
                        .templateId(templateId)
                        .recipientEntity(this)
                        .build()
        );
    }

    /**
     * Метод удаляет идентификатор шаблона из списка templateEntityIds текущего экземпляра RecipientEntity.
     * @param templateId идентификатор шаблона
     * @return текущий экземпляр RecipientEntity
     */
    public RecipientEntity removeTemplateEntity(Long templateId) {
        templateEntityIds.removeIf(
                // проверяем, равен ли templateId элемента списка переданному templateId.
                template -> Objects.equals(template.getTemplateId(), templateId)
        );
        return this;
    }
}
