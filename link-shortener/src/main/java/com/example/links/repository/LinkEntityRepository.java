package com.example.links.repository;

import com.example.links.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkEntityRepository extends JpaRepository<LinkEntity, Long>{


}
