package ru.moore.social_media_api.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.moore.social_media_api.DTO.utils.OnSave;
import ru.moore.social_media_api.DTO.utils.View;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Сущность для чата")
public class ChatDTO {

    @JsonView(View.RESPONSE.class)
    @Null(groups = OnSave.class, message = "Поле 'id' должно быть пустым.")
    @Schema(description = "id записи")
    private UUID id;

    @JsonView(View.SAVE.class)
    @NotBlank(groups = OnSave.class, message = "Значение 'message' не может быть пустым")
    @Schema(description = "Текст сообщения")
    private String message;

    @JsonView({View.RESPONSE.class})
    @Schema(description = "Отправитель сообщения")
    private AccountDTO accountFrom;

    @JsonView(View.SAVE.class)
    @NotNull(groups = OnSave.class, message = "Значение 'accountToId' не может быть пустым")
    @Schema(description = "Id получателя сообщения")
    private UUID accountToId;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Получатель сообщения")
    private AccountDTO accountTo;
}
