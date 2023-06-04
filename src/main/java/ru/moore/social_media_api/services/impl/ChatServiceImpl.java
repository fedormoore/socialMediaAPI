package ru.moore.social_media_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.moore.social_media_api.DTO.ChatDTO;
import ru.moore.social_media_api.models.Account;
import ru.moore.social_media_api.models.Chat;
import ru.moore.social_media_api.repositories.ChatRepository;
import ru.moore.social_media_api.services.AuthService;
import ru.moore.social_media_api.services.ChatService;
import ru.moore.social_media_api.utils.MapperUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final AuthService authService;

    private final MapperUtils mapperUtils;

    private final ChatRepository chatRepository;

    /**
     * Метод позволяет отправить сообщения в чат
     *
     * @param chatDTO        принимает в качестве параметра ChatDTO
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public ResponseEntity<String> newMessage(ChatDTO chatDTO, Authentication authentication) {
        Account accountTo = authService.findById(chatDTO.getAccountToId()).get();
        Account accountFrom = authService.getUserPrincipal(authentication);

        Chat chat = new Chat();
        chat.setAccountFrom(accountFrom);
        chat.setAccountTo(accountTo);
        chat.setMessage(chatDTO.getMessage());

        chatRepository.save(chat);
        return new ResponseEntity<>("Сообщение отправлено", HttpStatus.OK);
    }

    /**
     * Метод позволяет получить список сообщений в чате
     *
     * @param friendToId     id пользователя с кем ведется переписка
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public List<ChatDTO> getAllChatByFriendId(UUID friendToId, Authentication authentication) {
        List<Chat> chat = chatRepository.findAllByAccountFromIdInAndAccountToIdIn(Arrays.asList(authService.getUserPrincipal(authentication).getId(), friendToId), Arrays.asList(authService.getUserPrincipal(authentication).getId(), friendToId));
        List<ChatDTO> chatDTOList = mapperUtils.mapAll(chat, ChatDTO.class);
        return chatDTOList;
    }
}
