package ru.moore.social_media_api.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Schema(description = "Сущность для авторизации пользователя")
public class SignInRequestDTO {

    @Email(message = "Некорректный E-mail")
    @NotBlank(message = "Значение 'e-mail' не может быть пустым")
    @Schema(description = "E-mail пользователя")
    private String email;

    @NotBlank(message = "Значение 'password' не может быть пустым")
    @Size(min = 6, max = 30, message = "Пароль должен состоять не менее, чем из 6 символов, и не более 30 символов, и содержать заглавные, строчные буквы, а также цифры.")
    @Schema(description = "Пароль пользователя")
    private String password;

}
