package com.example.security.entity;

import com.example.security.model.AuthTokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.security.model.AuthTokenType.ACCESS_TOKEN;


/**
 * Сущность, представляющая аутентификационный токен.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_tokens")
public class AuthTokenEntity implements BaseEntity<Long>{

    /**
     * Уникальный идентификатор токена.
     */
    @Id
    @GeneratedValue
    public Long Id;

    /**
     * Клиент, связанный с токеном.
     */
    @OneToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;

    /**
     * Тип аутентификационного токена.
     */
    @Enumerated(EnumType.STRING)
    public final AuthTokenType tokenType = ACCESS_TOKEN;

    /**
     * Значение токена. Уникальное для каждого токена.
     */
    @Column(unique = true)
    public String tokenValue;

    /**
     * Признак отзыва токена доступа.
     */
    public boolean isRevoked; // отозван ли токен доступа.

    /**
     * Признак истечения срока действия токена доступа.
     */
    public boolean isExpired; // истек ли срок действия токена доступа.

}
