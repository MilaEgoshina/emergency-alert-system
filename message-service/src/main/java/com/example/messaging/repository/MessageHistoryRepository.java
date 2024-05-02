package com.example.messaging.repository;

import com.example.messaging.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Репозиторий для сохранения и получения истории сообщений.
 */
@Service
public interface MessageHistoryRepository extends JpaRepository<MessageLog,Long> {
}
