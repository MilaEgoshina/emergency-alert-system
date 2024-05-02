package com.example.templ.repository;

import com.example.templ.entity.RecipientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс репозитория для работы с сущностью RecipientId.
 */
@Repository
public interface RecipientIdRepository extends JpaRepository<RecipientId, Long> {

    /**
     * Проверяет, существует ли получатель с указанным идентификатором для указанного шаблона.
     *
     * @param templateId идентификатор шаблона
     * @param recipientId идентификатор получателя
     * @return true, если получатель существует для указанного шаблона, иначе false
     */
    Boolean checkIfRecipientExistsForTemplateIdAndRecipientId(Long templateId, Long recipientId);
}