package com.example.links.repository;

import com.example.links.entity.ResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseEntityRepository extends JpaRepository<ResponseEntity, Long>{


}
