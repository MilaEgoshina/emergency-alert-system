package com.example.templ.repository;

import com.example.templ.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для выполнения различных операций с шаблонами в базе данных, таких как поиск, проверка существования и т.д.
 */
@Repository
public interface TemplateEntityRepository extends JpaRepository<TemplateEntity, Long> {

    /**
     * Метод для поиска шаблона (Template) по его идентификатору (templateId) и идентификатору клиента (clientId).
     *
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @return возвращает Optional<Template>, что означает, что результат может быть пустым, если не найден подходящий шаблон.
     */
    Optional<TemplateEntity> getByIdAndClientId(Long templateId, Long clientId);


    /**
     * Метод проверяет, существует ли шаблон с указанным идентификатором (templateId), который содержит получателя с
     * указанным идентификатором (recipientId).
     *
     * @param templateId идентификатор шаблона
     * @param recipientId идентификатор получателя
     * @return возвращает true, в случае если шаблон найден.
     */
    Boolean checkIfTemplateExistsForRecipientAndTemplate(Long templateId, Long recipientId);

    /**
     * Метод проверяет, существует ли шаблон с указанным идентификатором клиента (clientId) и заголовком (title).
     *
     * @param clientId идентификатор клиента.
     * @param title заголовок шаблона.
     * @return возвращает true, в случае если шаблон найден.
     */
    Boolean checkIfTemplateExistsForClientAndTitle(Long clientId, String title);
}
