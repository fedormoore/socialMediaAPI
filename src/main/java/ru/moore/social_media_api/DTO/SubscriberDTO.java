package ru.moore.social_media_api.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.moore.social_media_api.DTO.utils.View;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Сущность для подписок на пользователей")
public class SubscriberDTO {

    @JsonView(View.RESPONSE.class)
    @Schema(description = "id записи")
    private UUID id;

    @JsonView(View.RESPONSE.class)
    @Schema(description = "Пользователя на которого подписываемся")
    private AccountDTO accountTo;
}
