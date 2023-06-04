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
import ru.moore.social_media_api.DTO.*;
import ru.moore.social_media_api.DTO.utils.OnDelete;
import ru.moore.social_media_api.DTO.utils.OnSave;
import ru.moore.social_media_api.DTO.utils.OnUpdate;
import ru.moore.social_media_api.DTO.utils.View;
import ru.moore.social_media_api.services.RequestFriendshipService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/request_friendship")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Название контроллера: request_friendship", description = "Контроллер служит для создания, подтверждения или отклонения запросов в друзья")
public class RequestFriendshipController {

    private final RequestFriendshipService requestFriendshipService;

    @Operation(
            summary = "Получение списка запросов в друзья",
            description = "Позволяет получить список запросов в друзья"
    )
    @GetMapping
    @JsonView(View.RESPONSE.class)
    public ResponseEntity<List<RequestFriendshipDTO>> getAllRequest(Authentication authentication) {
        return ResponseEntity.ok(requestFriendshipService.getAllRequest(authentication));
    }

    @Operation(
            summary = "Новый запрос в друзья",
            description = "Позволяет создать новый запрос в друзья"
    )
    @PostMapping(value = "/new_request/{friendToId}")
    public ResponseEntity<RequestFriendshipDTO> newRequest(@PathVariable("friendToId") @Parameter(description = "id пользователя которого добавляем в друзья") UUID friendToId, Authentication authentication) {
        return ResponseEntity.ok(requestFriendshipService.newRequest(friendToId, authentication));
    }

    @Operation(
            summary = "Подтверждение запроса на добавления в друзья",
            description = "Позволяет подтвердить запрос на добавления в друзья"
    )
    @PutMapping(value = "/confirm_request/{requestId}")
    public ResponseEntity<String> confirmRequest(@PathVariable("requestId") @Parameter(description = "id запроса") UUID requestId, Authentication authentication) {
        return requestFriendshipService.confirmRequest(requestId, authentication);
    }

    @Operation(
            summary = "Отклонить запрос в добавления в друзья",
            description = "Позволяет отклонить запрос на добавления в друзья"
    )
    @PutMapping(value = "/rejected_request/{requestId}")
    public ResponseEntity<String> rejectedRequest(@PathVariable("requestId") @Parameter(description = "id запроса") UUID requestId, Authentication authentication) {
        return requestFriendshipService.rejectedRequest(requestId, authentication);
    }

}
