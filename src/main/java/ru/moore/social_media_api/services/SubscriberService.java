package ru.moore.social_media_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.moore.social_media_api.DTO.SubscriberDTO;
import ru.moore.social_media_api.models.Subscriber;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriberService {

    /**
     * Метод позволяет получить список подписок на пользователей
     *
     * @param authentication пользователь авторизованной сессии
     */
    List<SubscriberDTO> getAllSubscribers(Authentication authentication);

    /**
     * Метод позволяет создать новую подписку на пользователя
     *
     * @param newFriendId    id пользователя на которого подписываемся
     * @param authentication пользователь авторизованной сессии
     */
    ResponseEntity<String> newSubscribers(UUID newFriendId, Authentication authentication);

    /**
     * Метод позволяет удалить подписку на пользователя
     *
     * @param subscriberId id подписки
     * @param authentication пользователь авторизованной сессии
     */
    ResponseEntity<String> deleteSubscribers(UUID subscriberId, Authentication authentication);

    /**
     * Метод позволяет проверить действительно ли подписка принадлежит пользователю
     *
     * @param id            id подписки
     * @param accountFromId id пользователь
     */
    Optional<Subscriber> findByIdAndAccountFromId(UUID id, UUID accountFromId);

    /**
     * Метод позволяет проверить действительно ли есть подписка на пользователя
     *
     * @param accountFromId id пользователь кто подписан
     * @param accountToId   id пользователь на кого подписан
     */
    Optional<Subscriber> findByAccountFromIdAndAccountToId(UUID accountFromId, UUID accountToId);

    /**
     * Метод позволяет получить список подписок пользователя
     *
     * @param id id пользователя
     */
    List<Subscriber> findAllByAccountFromId(UUID id);
}
