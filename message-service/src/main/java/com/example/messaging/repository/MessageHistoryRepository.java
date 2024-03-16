package com.example.messaging.repository;

import com.example.messaging.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface MessageHistoryRepository extends JpaRepository<MessageLog,Long> {
}
