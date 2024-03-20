package com.example.security.repository;


import com.example.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для работы с данными в базе данных, связанными с сущностью Customer.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    /**
     * Метод для поиска Customer по адресу электронной почты.
     * @param email адрес электронной почты клиента.
     * @return возвращает объект типа Optional, который может содержать AccessTokenEntity или быть пустым, если объект не найден.
     */
    Optional<Customer> getByEmail(String email);
}
