package ru.moore.social_media_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.moore.social_media_api.DTO.ChatDTO;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    /**
     * Метод позволяет отправить сообщения в чат
     *
     * @param chatDTO        принимает в качестве параметра ChatDTO
     * @param authentication пользователь авторизованной сессии
     */
    ResponseEntity<String> newMessage(ChatDTO chatDTO, Authentication authentication);

    /**
     * Метод позволяет получить список сообщений в чате
     *
     * @param friendToId     id пользователя с кем ведется переписка
     * @param authentication пользователь авторизованной сессии
     */
    List<ChatDTO> getAllChatByFriendId(UUID friendToId, Authentication authentication);
}
