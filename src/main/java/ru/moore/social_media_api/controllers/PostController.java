package ru.moore.social_media_api.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.moore.social_media_api.DTO.*;
import ru.moore.social_media_api.DTO.utils.OnDelete;
import ru.moore.social_media_api.DTO.utils.OnSave;
import ru.moore.social_media_api.DTO.utils.OnUpdate;
import ru.moore.social_media_api.DTO.utils.View;
import ru.moore.social_media_api.services.PostService;

import javax.validation.Valid;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Название контроллера: post", description = "Контроллер служит для обработки запросов на получение, создание, обновление и удаление постов")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Получение списка постов",
            description = "Позволяет получить список постов пользователей, на которых есть подписка"
    )
    @GetMapping
    public ResponseEntity<Page<PostDTO>> getPostSubscriptions(@RequestParam(name = "sorted", defaultValue = "asc") @Parameter(description = "Сортировка постов. Варианты значений: asc, desc. По умолчанию asc.") String sorted,
                                                              @RequestParam(name = "page", defaultValue = "1") @Parameter(description = "Пагинация, страница. Варианты значений: любое положительное число. По умолчанию 1.") int page,
                                                              @RequestParam(name = "limit", defaultValue = "2") @Parameter(description = "Пагинация, количество записей на странице. Варианты значений: любое положительное число. По умолчанию 2.") int limit,
                                                              Authentication authentication) {
        if (page < 1) {
            page = 1;
        }
        return ResponseEntity.ok(postService.getPostSubscriptions(sorted, page, limit, authentication));
    }

    @Operation(
            summary = "Создание нового поста",
            description = "Позволяет создать новый пост"
    )
    @PostMapping(consumes = {"multipart/form-data"})
    @JsonView(View.RESPONSE.class)
    @Validated(OnSave.class)
    public ResponseEntity<PostDTO> newPost(@RequestPart("file") @Parameter(description = "Возможность прикрепить изображение к посту. Параметр-file.") MultipartFile file,
                                           @JsonView(View.SAVE.class) @Valid @RequestPart("data") @Parameter(description = "Пост. Параметр-data") PostDTO postDTO,
                                           Authentication authentication) {
        return ResponseEntity.ok(postService.newPost(postDTO, file, authentication));
    }

    @Operation(
            summary = "Обновление поста",
            description = "Позволяет обновить существующий пост"
    )
    @PutMapping
    @JsonView(View.RESPONSE.class)
    @Validated(OnUpdate.class)
    public ResponseEntity<PostDTO> updatePost(@JsonView(View.UPDATE.class) @Valid @RequestBody PostDTO postDTO, Authentication authentication) {
        return ResponseEntity.ok(postService.updatePost(postDTO, authentication));
    }

    @Operation(
            summary = "Удаление поста",
            description = "Позволяет удалить пост"
    )
    @DeleteMapping
    @Validated(OnDelete.class)
    public ResponseEntity<String> deletePost(@JsonView(View.DELETE.class) @Valid @RequestBody PostDTO postDTO, Authentication authentication) {
        return postService.deletePost(postDTO, authentication);
    }

}
