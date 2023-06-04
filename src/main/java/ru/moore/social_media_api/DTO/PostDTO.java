package ru.moore.social_media_api.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.moore.social_media_api.DTO.utils.OnDelete;
import ru.moore.social_media_api.DTO.utils.OnSave;
import ru.moore.social_media_api.DTO.utils.OnUpdate;
import ru.moore.social_media_api.DTO.utils.View;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Сущность для поста")
public class PostDTO {

    @JsonView({View.RESPONSE.class, View.UPDATE.class})
    @Null(groups = OnSave.class, message = "Поле 'id' должно быть пустым.")
    @NotNull(groups = {OnUpdate.class, OnDelete.class}, message = "Поле 'id' не может быть пустым.")
    @Schema(description = "id записи")
    private UUID id;

    @JsonView({View.RESPONSE.class, View.SAVE.class, View.UPDATE.class})
    @NotBlank(groups = {OnSave.class, OnUpdate.class}, message = "Значение 'headerPost' не может быть пустым")
    @Schema(description = "Заголовок поста")
    private String headerPost;

    @JsonView({View.RESPONSE.class, View.SAVE.class, View.UPDATE.class})
    @NotBlank(groups = {OnSave.class, OnUpdate.class}, message = "Значение 'textPost' не может быть пустым")
    @Schema(description = "Текс поста")
    private String textPost;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Автор поста")
    private AccountDTO author;
}
