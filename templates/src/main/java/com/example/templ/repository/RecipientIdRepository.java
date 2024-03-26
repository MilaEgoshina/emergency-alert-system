package com.example.templ.repository;

import com.example.templ.entity.RecipientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientIdRepository extends JpaRepository<RecipientId, Long> {

    Boolean checkIfRecipientExistsForTemplateIdAndRecipientId(Long templateId, Long recipientId);
}