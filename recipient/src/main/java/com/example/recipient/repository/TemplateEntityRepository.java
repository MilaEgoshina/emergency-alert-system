package com.example.recipient.repository;

import com.example.recipient.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateEntityRepository extends JpaRepository<TemplateEntity, Long> {

    Boolean existsByTemplateEntityAndRecipientId(Long templateId, Long recipientId);

    List<TemplateEntity> getAllByRecipient_clientIdAndTemplateEntity(Long clientId, Long templateId);
}
