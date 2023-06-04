package ru.moore.social_media_api.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.moore.social_media_api.DTO.ChatDTO;
import ru.moore.social_media_api.DTO.utils.OnSave;
import ru.moore.social_media_api.DTO.utils.View;
import ru.moore.social_media_api.services.ChatService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Название контроллера: chat", description = "Контроллер служит для переписки между друзьями")
public class ChatController {

    private final ChatService chatService;

    @Operation(
            summary = "Получение списка сообщений в чате",
            description = "Позволяет получить список сообщений в чате"
    )
    @GetMapping(value = "/{friendToId}")
    @JsonView(View.RESPONSE.class)
    public ResponseEntity<List<ChatDTO>> getAllChatByFriendId(@PathVariable("friendToId") @Parameter(description = "Идентификатор пользователя") UUID friendToId, Authentication authentication) {
        return ResponseEntity.ok(chatService.getAllChatByFriendId(friendToId, authentication));
    }

    @Operation(
            summary = "Отправка сообщения",
            description = "Позволяет отправить сообщения в чат"
    )
    @PostMapping
    @JsonView(View.RESPONSE.class)
    @Validated(OnSave.class)
    public ResponseEntity<String> newMessage(@JsonView(View.SAVE.class) @Valid @RequestBody ChatDTO chatDTO, Authentication authentication) {
        return chatService.newMessage(chatDTO, authentication);
    }

}
