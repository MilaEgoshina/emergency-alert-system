package com.example.links.repository;

import com.example.links.entity.ResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс, который представляет репозиторий для сущности ResponseEntity.
 * Реализует операции доступа к данным с использованием Spring Data JPA.
 */
@Repository
public interface ResponseEntityRepository extends JpaRepository<ResponseEntity, Long>{


}
