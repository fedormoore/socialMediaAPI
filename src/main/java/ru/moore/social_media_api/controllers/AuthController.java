package ru.moore.social_media_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moore.social_media_api.DTO.auth.JwtResponseDTO;
import ru.moore.social_media_api.DTO.auth.RefreshJwtRequestDTO;
import ru.moore.social_media_api.DTO.auth.SignInRequestDTO;
import ru.moore.social_media_api.DTO.auth.SignUpRequestDTO;
import ru.moore.social_media_api.services.AuthService;

import javax.validation.Valid;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Название контроллера: auth", description = "Контроллер служит для аутентификации и авторизации пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать нового пользователя"
    )
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        return authService.signUp(signUpRequestDTO);
    }

    @Operation(
            summary = "Обновление JWT токена",
            description = "Позволяет обновить устаревший JWT токен"
    )
    @PostMapping("/refresh-tokens")
    public ResponseEntity<JwtResponseDTO> refreshTokens(@Valid @RequestBody RefreshJwtRequestDTO refreshJwtRequestDTO) {
        JwtResponseDTO jwtResponseDTO = authService.refreshJWTToken(refreshJwtRequestDTO.getRefreshToken());
        return ResponseEntity.ok(jwtResponseDTO);
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Позволяет аутентифицировать и авторизовать пользователя"
    )
    @PostMapping("/signIn")
    public ResponseEntity<JwtResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        JwtResponseDTO jwtResponseDTO = authService.signIn(signInRequestDTO.getEmail(), signInRequestDTO.getPassword());
        return ResponseEntity.ok(jwtResponseDTO);
    }

}
