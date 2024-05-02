package com.example.recipient.repository;

import com.example.recipient.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для сущности получателя.
 * Предоставляет методы для работы с базой данных для сущности получателя.
 */
@Repository
public interface RecipientEntityRepository extends JpaRepository<RecipientEntity, Long> {

    /**
     * Получает получателя по адресу электронной почты и идентификатору клиента.
     *
     * @param email Адрес электронной почты получателя.
     * @param clientId Идентификатор клиента.
     * @return Получатель, удовлетворяющий условиям поиска (или пустой результат, если не найден).
     */
    Optional<RecipientEntity> getByEmailAndClientId(String email, Long clientId);

    /**
     * Получает получателя по идентификатору и идентификатору клиента.
     *
     * @param recipientId Идентификатор получателя.
     * @param clientId Идентификатор клиента.
     * @return Получатель, удовлетворяющий условиям поиска (или пустой результат, если не найден).
     */
    Optional<RecipientEntity> getByIdAndClientId(Long recipientId, Long clientId);

    /**
     * Получает всех получателей для указанного клиента.
     *
     * @param clientId Идентификатор клиента.
     * @return Список всех получателей для указанного клиента.
     */
    List<RecipientEntity> getAllByClientId(Long clientId);
}
