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
import ru.moore.social_media_api.DTO.SubscriberDTO;
import ru.moore.social_media_api.DTO.utils.View;
import ru.moore.social_media_api.services.SubscriberService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/subscribers")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Название контроллера: subscribers", description = "Контроллер служит для получения и удаления подписок на пользователей")
public class SubscriberController {

    private final SubscriberService subscriberService;

    @Operation(
            summary = "Получение списка подписок на пользователей",
            description = "Позволяет получить список подписок на пользователей"
    )
    @GetMapping
    @JsonView(View.RESPONSE.class)
    public ResponseEntity<List<SubscriberDTO>> getAllSubscribers(Authentication authentication) {
        return ResponseEntity.ok(subscriberService.getAllSubscribers(authentication));
    }

    @Operation(
            summary = "Удаление подписки на пользователя",
            description = "Позволяет удалить подписку на пользователя"
    )
    @DeleteMapping(value = "/{subscriberId}")
    public ResponseEntity<String> deleteSubscribers(@PathVariable("subscriberId") @Parameter(description = "id подписки") UUID subscriberId, Authentication authentication) {
        return subscriberService.deleteSubscribers(subscriberId, authentication);
    }

}
