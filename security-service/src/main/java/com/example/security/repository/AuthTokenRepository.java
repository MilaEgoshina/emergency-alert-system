package com.example.security.repository;

import com.example.security.entity.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для работы с данными в базе данных, связанными с сущностью AuthTokenEntity.
 */
@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity,Long> {

    /**
     * Метод для получения объекта AuthTokenEntity по идентификатору клиента.
     * @param id идентификатор клиента.
     * @return возвращает объект типа Optional, который может содержать AuthTokenEntity или быть пустым, если объект не найден.
     */
    Optional<AuthTokenEntity> getByClientId(Long id);

    /**
     * Метод для получения объекта AuthTokenEntity по JWT-токену.
     * @param jwt JWT-токен.
     * @return возвращает объект типа Optional, который может содержать AuthTokenEntity или быть пустым, если объект не найден.
     */
    Optional<AuthTokenEntity> getByJwt(String jwt);
}
