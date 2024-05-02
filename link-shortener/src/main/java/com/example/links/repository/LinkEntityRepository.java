package com.example.links.repository;

import com.example.links.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс, который представляет репозиторий для сущности LinkEntity.
 * Реализует операции доступа к данным с использованием Spring Data JPA.
 */
@Repository
public interface LinkEntityRepository extends JpaRepository<LinkEntity, Long>{


}
