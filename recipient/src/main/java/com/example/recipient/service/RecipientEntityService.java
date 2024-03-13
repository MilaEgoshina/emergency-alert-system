package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientEntityRequest;
import com.example.recipient.dto.response.RecipientEntityResponse;
import com.example.recipient.entity.RecipientEntity;
import com.example.recipient.exception.RecipientEntityNotFoundException;
import com.example.recipient.exception.RecipientEntityRegistrationError;
import com.example.recipient.mapper.RecipientEntityMapper;
import com.example.recipient.repository.RecipientEntityRepository;
import com.example.recipient.repository.TemplateEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *  Класс - сервис для управления получателями сообщений.
 */

@Service
@RequiredArgsConstructor
public class RecipientEntityService {

    private RecipientEntityRepository recipientEntityRepository;
    private TemplateEntityRepository templateEntityRepository;

    private MessageService messageService;
    private RecipientEntityMapper recipientEntityMapper;

    /**
     * Метод для регистрации нового получателя.
     * @param clientId идентификатор клиента
     * @param entityRequest запрос в виде экземпляра класса RecipientEntityRequest
     * @return возвращает объект RecipientEntityResponse, который содержит данные о зарегистрированном получателе.
     */
    public RecipientEntityResponse register(Long clientId, RecipientEntityRequest entityRequest) {

        // сначала проверяем, существует ли получатель с указанным адресом электронной почты и идентификатором клиента.
        Optional<RecipientEntity> existingRecipient = recipientEntityRepository.getByEmailAndClientId(entityRequest.getEmail(), clientId);
        if (existingRecipient.isPresent()) {

            //Если получатель уже существует, он обновляется с помощью метода update.
            return update(clientId, existingRecipient.get().getId(), entityRequest);
        }
        // Если получатель не существует, он создается и регистрируется.
        try {
            // Преобразуем запрос RecipientRequest в объект сущности RecipientEntityRequest.
            RecipientEntity recipientEntity = recipientEntityMapper.toEntity(entityRequest).addClientId(clientId);

            //Сохраняем получателя в базе данных и возвращаем его в виде RecipientEntityResponse.
            RecipientEntity savedRecipient = recipientEntityRepository.save(recipientEntity);
            return recipientEntityMapper.toDTO(savedRecipient);
        } catch (DataIntegrityViolationException e) {
            throw new RecipientEntityRegistrationError("Recipient with the this email already exists.");
        }
    }

    /**
     * Метод для получения получателя по его идентификатору и идентификатору клиента.
     * @param clientId идентификатор клиента
     * @param recipientId идентификатор получателя
     * @return возвращается искомый получатель в виде объекта RecipientEntityResponse.
     */
    public RecipientEntityResponse receive(Long clientId, Long recipientId) {
        return recipientEntityRepository.getByIdAndClientId(recipientId, clientId)
                .map(recipientEntityMapper::toDTO)
                .orElseThrow(() -> new RecipientEntityNotFoundException("Recipient not found."));
    }

    /**
     * Метод для удаления получателя по его идентификатору и идентификатору клиента.
     * @param clientId идентификатор клиента
     * @param recipientId идентификатор получателя
     * @return возвращает true, если получатель был удален, и false в противном случае.
     */
    public boolean delete(Long clientId, Long recipientId) {
        return recipientEntityRepository.getByIdAndClientId(recipientId, clientId)
                .map(recipient -> {
                    recipientEntityRepository.delete(recipient);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Метод обновляет существующего получателя по его идентификатору и идентификатору клиента.
     * @param clientId идентификатор клиента
     * @param recipientId идентификатор получателя
     * @param entityRequest запрос в виде экземпляра класса RecipientEntityRequest
     * @return возвращает объект RecipientEntityResponse, который содержит данные об обновленном получателе.
     */
    public RecipientEntityResponse update(Long clientId, Long recipientId, RecipientEntityRequest entityRequest) {
        try {
            Optional<RecipientEntity> recipient = recipientEntityRepository.getByIdAndClientId(recipientId, clientId);
            if (recipient.isPresent()) {
                //Если получатель найден, метод обновляет его поля в соответствии с предоставленным запросом RecipientEntityRequest.
                recipientEntityMapper.update(entityRequest, recipient.get());
                // Сохраняем обновленного получателя в базе данных и возвращаем его в виде RecipientResponse.
                return recipientEntityMapper.toDTO(recipientEntityRepository.saveAndFlush(recipient.get()));
            } else {
                throw new RecipientEntityNotFoundException("Recipient not found.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new RecipientEntityRegistrationError("Recipient with the provided email already exists.");
        }
    }

    /**
     * Метод для получения получателей, связанных с определенным шаблоном сообщения.
     * @param clientId идентификатор клиента
     * @param templateId идентификатор шаблона
     * @return возвращает список объектов RecipientEntityResponse, которые содержат данные о получателях.
     */
    public List<RecipientEntityResponse> receiveByTemplateEntity(Long clientId, Long templateId) {

        // извлекаем получателей из базы данных, которые связаны с указанным шаблоном сообщения.
        return templateEntityRepository.getAllByRecipient_clientIdAndTemplateEntity(clientId, templateId)
                .stream()
                .map(TemplateEntity -> TemplateEntity.getRecipientEntity())
                .map(recipientEntityMapper::toDTO)
                .toList();
    }

    /**
     * Метод для получения всех получателей, связанных с определенным клиентом.
     * @param clientId идентификатор клиента
     * @return возвращает список объектов RecipientEntityResponse, которые содержат данные о получателях.
     */
    public List<RecipientEntityResponse> receiveByClient(Long clientId) {

        // извлекаем получателей из базы данных, которые связаны с указанным клиентом.
        return recipientEntityRepository.getAllByClientId(clientId)
                .stream()
                .map(recipientEntityMapper::toDTO)
                .toList();
    }
}
