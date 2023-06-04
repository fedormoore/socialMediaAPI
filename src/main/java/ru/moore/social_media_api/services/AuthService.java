package ru.moore.social_media_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import ru.moore.social_media_api.DTO.auth.JwtResponseDTO;
import ru.moore.social_media_api.DTO.auth.SignUpRequestDTO;
import ru.moore.social_media_api.models.Account;

import java.util.Optional;
import java.util.UUID;

public interface AuthService {

    /**
     * Метод позволяет зарегистрировать нового пользователя
     *
     * @param signUpRequestDTO принимает в качестве параметра SignUpRequestDTO
     */
    ResponseEntity<String> signUp(SignUpRequestDTO signUpRequestDTO);

    /**
     * Метод позволяет аутентифицировать и авторизовать пользователя
     *
     * @param email    email пользователя
     * @param password пароль пользователя
     */
    JwtResponseDTO signIn(String email, String password);

    /**
     * Метод позволяет обновить JWT токен
     *
     * @param refreshToken токен для обновления
     */
    JwtResponseDTO refreshJWTToken(String refreshToken);

    UserDetails loadUserByUsername(String login);

    /**
     * Метод позволяет получить информацию об авторизованном пользователе
     *
     * @param authentication пользователь авторизованной сессии
     */
    Account getUserPrincipal(Authentication authentication);

    /**
     * Метод позволяет проверить существует ли пользователь в базе данных
     *
     * @param id UUID пользователя
     */
    Optional<Account> findById(UUID id);

}
