package ru.moore.social_media_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.moore.social_media_api.DTO.RequestFriendshipDTO;
import ru.moore.social_media_api.models.RequestFriendship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestFriendshipService {

    /**
     * Метод позволяет получить список запросов в друзья
     *
     * @param authentication пользователь авторизованной сессии
     */
    List<RequestFriendshipDTO> getAllRequest(Authentication authentication);

    /**
     * Метод позволяет создать новый запрос в друзья
     *
     * @param friendToId id пользователя которого добавляем в друзья
     * @param authentication       пользователь авторизованной сессии
     */
    RequestFriendshipDTO newRequest(UUID friendToId, Authentication authentication);

    /**
     * Метод позволяет подтвердить запрос в друзья
     *
     * @param requestId id запроса
     * @param authentication       пользователь авторизованной сессии
     */
    ResponseEntity<String> confirmRequest(UUID requestId, Authentication authentication);

    /**
     * Метод позволяет отклонить запрос в друзья
     *
     * @param requestId id запроса
     * @param authentication       пользователь авторизованной сессии
     */
    ResponseEntity<String> rejectedRequest(UUID requestId, Authentication authentication);

    /**
     * Метод позволяет проверить существует ли запрос на добавления в друзья в базе данных
     *
     * @param id id запроса в друзья
     */
    Optional<RequestFriendship> findById(UUID id);

    /**
     * Метод позволяет проверить что запрос принадлежит пользователю
     *
     * @param @param         requestFriendshipDTO        принимает в качестве параметра RequestFriendshipDTO
     * @param authentication пользователь авторизованной сессии
     */
    void checkRequestToAccount(RequestFriendship requestFriendship, Authentication authentication);

}
