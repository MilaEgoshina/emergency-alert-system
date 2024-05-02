package com.example.recipient.repository;

import com.example.recipient.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями шаблонов (TemplateEntity).
 */
@Repository
public interface TemplateEntityRepository extends JpaRepository<TemplateEntity, Long> {

    /**
     * Проверяет, существует ли шаблон с указанным идентификатором и для указанного получателя.
     *
     * @param templateId Идентификатор шаблона.
     * @param recipientId Идентификатор получателя.
     * @return true, если шаблон существует для данного получателя, в противном случае - false.
     */
    Boolean existsByTemplateEntityAndRecipientId(Long templateId, Long recipientId);

    /**
     * Получает список всех шаблонов для указанного клиента и шаблона.
     *
     * @param clientId Идентификатор клиента.
     * @param templateId Идентификатор шаблона.
     * @return Список шаблонов для указанного клиента и шаблона.
     */
    List<TemplateEntity> getAllByRecipient_clientIdAndTemplateEntity(Long clientId, Long templateId);
}
