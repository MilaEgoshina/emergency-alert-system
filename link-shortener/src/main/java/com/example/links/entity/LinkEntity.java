package com.example.links.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Класс - сущность ссылки в базе данных.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "links")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ElementCollection(
            fetch = FetchType.EAGER
    )
    @CollectionTable(
            name = "link_option_mapping",
            joinColumns = @JoinColumn(name = "link_id")
    )
    @MapKeyColumn(
            name = "link_key"
    )
    @Column(name = "option")
    private Map<String, String> linksOptionMap = new HashMap<>();

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();
}
