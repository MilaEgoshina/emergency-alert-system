package com.example.messaging.entity;


import com.example.messaging.model.MessageState;
import com.example.messaging.model.MessageWay;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Класс для хранения информации об отправленных сообщениях и их состояниях, что позволяет отслеживать историю отправки
 * и обработки сообщений.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_logs")
public class MessageLog implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; // уникальный идентификатор записи в таблице message_logs

    private Long senderId; // идентификатор отправителя сообщения.
    private Long recipientId; // идентификатор получателя сообщения.
    private Long templateId; // идентификатор шаблона, который был использован для формирования сообщения.
    private Long linkId; // идентификатор ссылки, связанной с сообщением.

    private MessageWay messageWay; // способ отправки сообщения (SMS, электронная почта и т.д.), определяется перечислением MessageWay.
    private String credentials; // учетные данные, которые были использованы для отправки сообщения.

    private MessageState state; // состояние сообщения (создано, отправлено, доставлено и т.д.), определяется перечислением MessageState
    private Integer retryCount; // количество попыток отправки сообщения.
    private LocalDateTime createdOn; // дата и время создания сообщения.

    @Builder.Default
    private LocalDateTime sentOn = LocalDateTime.now(); // дата и время отправки сообщения.
    // По умолчанию устанавливается текущее время при создании объекта.

    public MessageLog setMessageState(MessageState messageState) {
        setState(messageState);
        return this;
    }

}
