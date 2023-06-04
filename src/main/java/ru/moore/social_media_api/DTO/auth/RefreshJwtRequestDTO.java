package ru.moore.social_media_api.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Schema(description = "Сущность для обновления JWT токена")
public class RefreshJwtRequestDTO {

    @NotBlank(message = "Значение 'refreshToken' не может быть пустым")
    @Schema(description = "RefreshToken токен")
    public String refreshToken;

}
