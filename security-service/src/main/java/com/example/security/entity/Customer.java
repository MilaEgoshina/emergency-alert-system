package com.example.security.entity;

import com.example.security.model.CustomerRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
// аннотация определяет имя таблицы (customers) и задает индекс (customers_idx_email) и уникальное ограничение
// (customers_unq_email) для столбца email
@Table(
        name = "customers",
        indexes = {
                @Index(name = "customers_idx_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "customers_unq_email", columnNames = "email")
        }
)
public class Customer implements UserDetails, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // аннотация указывает, что значение перечисления должно храниться в базе данных как строка.
    @Enumerated(EnumType.STRING)
    private CustomerRole customerRole;

    private String email;
    private String password;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Метод возвращает коллекцию объектов GrantedAuthority, представляющих роли и привилегии пользователя.
     * @return возвращает одноэлементный список, содержащий роль пользователя (Collections.singletonList(role)).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(customerRole);
    }
}
