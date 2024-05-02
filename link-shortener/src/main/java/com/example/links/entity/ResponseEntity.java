package com.example.links.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс - сущность ответа в базе данных.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responses")
public class ResponseEntity implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ElementCollection(
            targetClass = String.class,
            fetch = FetchType.EAGER
    )
    @CollectionTable(
            name = "options",
            joinColumns = @JoinColumn(name = "response_id")
    )
    @Column(
            name = "option", nullable = false
    )
    private List<String> options = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();
}
