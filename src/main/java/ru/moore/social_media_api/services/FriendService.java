package ru.moore.social_media_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.moore.social_media_api.DTO.FriendDTO;
import ru.moore.social_media_api.models.Friend;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendService {

    /**
     * Метод позволяет получить список друзей
     *
     * @param authentication пользователь авторизованной сессии
     */
    List<FriendDTO> getAllFriends(Authentication authentication);

    /**
     * Метод позволяет добавить пользователя в друзья
     *
     * @param friendFromId   id первого пользователя
     * @param friendToId id второго пользователя
     */
    ResponseEntity<String> newFriend(UUID friendFromId, UUID friendToId);

    /**
     * Метод позволяет удалить пользователя из друзей
     *
     * @param friendToId id друга с кем дружим
     * @param authentication пользователь авторизованной сессии
     */
    ResponseEntity<String> deleteFriend(UUID friendToId, Authentication authentication);

    /**
     * Метод позволяет получить запись о дружбе с пользователем
     *
     * @param friendFromId id пользователя кто дружит
     * @param friendToId id друга с кем дружим
     */
    Optional<Friend> findByAccountFromIdAndAccountToId(UUID friendFromId, UUID friendToId);
}
