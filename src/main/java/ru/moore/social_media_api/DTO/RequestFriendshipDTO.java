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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Сущность запросов в друзья")
public class RequestFriendshipDTO {

    @JsonView(View.RESPONSE.class)
    @Schema(description = "id записи")
    private UUID id;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Пользователя которого добавляем в друзья")
    private AccountDTO accountTo;

}
