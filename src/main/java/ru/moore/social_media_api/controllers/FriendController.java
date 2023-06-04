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
import ru.moore.social_media_api.DTO.FriendDTO;
import ru.moore.social_media_api.DTO.utils.View;
import ru.moore.social_media_api.services.FriendService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Название контроллера: friends", description = "Контроллер служит для получения списка друзей и удаления друга из списка")
public class FriendController {

    private final FriendService friendService;

    @Operation(
            summary = "Получение списка друзей",
            description = "Позволяет получить список друзей"
    )
    @GetMapping
    @JsonView(View.RESPONSE.class)
    public ResponseEntity<List<FriendDTO>> getAllFriends(Authentication authentication) {
        return ResponseEntity.ok(friendService.getAllFriends(authentication));
    }

    @Operation(
            summary = "Удаление из друзей",
            description = "Позволяет удалить пользователя из друзей"
    )
    @DeleteMapping(value = "/{friendId}")
    public ResponseEntity<String> deleteFriends(@PathVariable("friendId") @Parameter(description = "id друга") UUID friendId, Authentication authentication) {
        return friendService.deleteFriend(friendId, authentication);
    }

}
