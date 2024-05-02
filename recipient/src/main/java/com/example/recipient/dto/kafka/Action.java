package com.example.recipient.dto.kafka;

/**
 * Перечисление, определяющее действия, которые могут быть выполнены в сообщениях Kafka.
 */
public enum Action {
    /**
     * Действие для сохранения данных.
     */
    PERSISTS,
    /**
     * Действие для удаления данных.
     */
    REMOVE
}
