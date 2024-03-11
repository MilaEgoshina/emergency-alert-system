package com.example.recipient.repository;

import com.example.recipient.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipientEntityRepository extends JpaRepository<RecipientEntity, Long> {

    Optional<RecipientEntity> getByEmailAndClientId(String email, Long clientId);

    Optional<RecipientEntity> getByIdAndClientId(Long recipientId, Long clientId);

    List<RecipientEntity> getAllByClientId(Long clientId);
}
