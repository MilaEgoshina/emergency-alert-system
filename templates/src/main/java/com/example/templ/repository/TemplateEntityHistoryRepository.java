package com.example.templ.repository;

import com.example.templ.entity.TemplateHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для выполнения различных операций с историей шаблонов в базе данных, таких как поиск по различным критериям.
 */
@Repository
public interface TemplateEntityHistoryRepository extends JpaRepository<TemplateHistoryEntity,Long> {

    /**
     * Метод для поиска истории шаблона по его идентификатору и идентификатору клиент.
     *
     * @param templateHistoryId идентификатор истории шаблона.
     * @param clientId идентификатор клиента шаблона.
     * @return возвращает Optional<TemplateHistory>, что означает, что результат может быть пустым, если не найдена
     * подходящая история шаблона.
     */
    Optional<TemplateHistoryEntity> getByIdAndClientId(Long templateHistoryId, Long clientId);

    /**
     * Метод для поиска истории шаблона по идентификатору клиента, идентификатору ответа, заголовку и содержимому.
     *
     * @param clientId идентификатор клиента шаблона.
     * @param responseId идентификатор ответа шаблона.
     * @param header заголовок шаблона.
     * @param details содержимое самого шаблона.
     * @return возвращает Optional<TemplateHistory>, что означает, что результат может быть пустым, если не найдена
     * подходящая история шаблона.
     */
    Optional<TemplateHistoryEntity> getByClientIdAndResponseIdAndHeaderAndDetails(
            Long clientId,
            Long responseId,
            String header,
            String details
    );
}
