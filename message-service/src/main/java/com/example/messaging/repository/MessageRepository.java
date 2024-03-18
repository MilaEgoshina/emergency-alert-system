package com.example.messaging.repository;

import com.example.messaging.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для выполнения запросов к базе данных для сущности Message.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Метод используется для поиска уведомления по идентификатору сообщения и идентификатору отправителя.
     * @param messageId идентификатора сообщения.
     * @param senderId идентификатор отправителя.
     * @return возвращает Optional<Message>, что означает, что уведомление может быть найдено или не найдено.
     */
    Optional<Message> getByIdAndSenderId(Long messageId, Long senderId);

    /**
     * Метод, который позволяет выполнять поиск уведомлений с определенным статусом и временем создания.
     * @param pendingDateTime время ожидания получения сообщения для фильтрации уведомлений по времени создания
     * @param newDateTime новое время сообщения для фильтрации уведомлений по времени создания
     * @param pageable позволяет задать настройки пагинации (номер страницы, размер страницы и сортировку).
     * @return возвращает список сообщений, удовлетворяющих условию.
     */
    @Query("""
            SELECT m
            FROM Message m
            WHERE
            m.messageState = 'R'
                OR
            m.messageState = 'C' AND m.createdOn < :newDateTime
                OR
            m.messageState = 'P' AND m.createdOn  < :pendingDateTime
            """)
    List<Message> getMessagesByStateAndCreatedOn(
            @Param("pendingDateTime") LocalDateTime pendingDateTime,
            @Param("newDateTime") LocalDateTime newDateTime,
            Pageable pageable
    );
}
