package com.example.security.repository;

import com.example.security.entity.AccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для работы с данными в базе данных, связанными с сущностью AccessTokenEntity.
 */
@Repository
public interface AuthTokenRepository extends JpaRepository<AccessTokenEntity,Long> {

    /**
     * Метод для получения объекта AccessTokenEntity по идентификатору клиента.
     * @param id идентификатор клиента.
     * @return возвращает объект типа Optional, который может содержать AccessTokenEntity или быть пустым, если объект не найден.
     */
    Optional<AccessTokenEntity> getByClientId(Long id);

    /**
     * Метод для получения объекта AccessTokenEntity по JWT-токену.
     * @param jwt JWT-токен.
     * @return возвращает объект типа Optional, который может содержать AccessTokenEntity или быть пустым, если объект не найден.
     */
    Optional<AccessTokenEntity> getByJwt(String jwt);
}
