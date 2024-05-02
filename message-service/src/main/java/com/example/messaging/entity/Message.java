package com.example.messaging.entity;

import com.example.messaging.model.MessageState;
import com.example.messaging.model.MessageWay;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Класс Message представляет сообщение, которое будет храниться в базе данных.
 * Каждое сообщение имеет уникальный идентификатор, отправителя, получателя, шаблон, ссылку,
 * способ отправки, учетные данные, состояние, количество попыток отправки и дату создания.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
// сущность (entity), которая будет отображаться на таблицу messages в базе данных.
public class Message implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; // идентификатор сообщения, который является первичным ключом в таблице.

    private Long senderId; // идентификатор отправителя сообщения.
    private Long recipientId; // идентификатор получателя сообщения.
    private Long templateId; // идентификатор шаблона, используемого для формирования сообщения.
    private Long linkId; // идентификатор ссылки, связанной с сообщением.

    private MessageWay messageWay; // способ отправки сообщения (SMS, электронная почта и т.д.), определяется перечислением MessageWay.
    private String credentials; // учетные данные, необходимые для отправки сообщения (данные для аутентификации).

    // состояние сообщения (создано, отправлено, доставлено и т.д.), определяется перечислением MessageState.
    // По умолчанию устанавливается значение MessageState.CREATED.
    @Builder.Default
    private MessageState messageState = MessageState.CREATED;

    // Количество попыток отправки сообщения. По умолчанию равно 0.
    @Builder.Default
    private Integer retryCount = 0;

    // Дата и время создания сообщения. По умолчанию устанавливается текущее время при создании объекта.
    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    public Message setMessageState(MessageState messageState) {
        setMessageState(messageState);
        return this;
    }

    public Message updateCreationTime() {
        setCreatedOn(LocalDateTime.now());
        return this;
    }

    public Message incrementRetryCount() {
        setRetryCount(getRetryCount() + 1);
        return this;
    }

    /**
     * Метод, который связывает сообщение с шаблоном, устанавливая идентификатор шаблона, и возвращает текущий объект Message.
     * @param templateId идентификатор шаблона сообщения
     * @return возвращает текущий объект Message
     */
    public Message associateTemplate(Long templateId) {
        setTemplateId(templateId);
        return this;
    }

    public Message setLink(Long linkId) {
        setLinkId(linkId);
        return this;
    }

}
