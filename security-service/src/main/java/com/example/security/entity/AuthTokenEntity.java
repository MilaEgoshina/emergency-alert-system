package com.example.security.entity;

import com.example.security.model.AuthTokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.security.model.AuthTokenType.ACCESS_TOKEN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_tokens")
public class AuthTokenEntity implements BaseEntity<Long>{

    @Id
    @GeneratedValue
    public Long Id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;

    @Enumerated(EnumType.STRING)
    public final AuthTokenType tokenType = ACCESS_TOKEN;

    @Column(unique = true)
    public String tokenValue;

    public boolean isRevoked; // отозван ли токен доступа.
    public boolean isExpired; // истек ли срок действия токена доступа.

}
