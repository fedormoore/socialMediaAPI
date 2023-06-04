package ru.moore.social_media_api.services.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moore.social_media_api.DTO.auth.JwtResponseDTO;
import ru.moore.social_media_api.DTO.auth.SignUpRequestDTO;
import ru.moore.social_media_api.exeptions.ErrorTemplate;
import ru.moore.social_media_api.models.Account;
import ru.moore.social_media_api.repositories.AccountRepository;
import ru.moore.social_media_api.security.JwtProvider;
import ru.moore.social_media_api.security.UserPrincipal;
import ru.moore.social_media_api.services.AuthService;
import ru.moore.social_media_api.utils.MapperUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements UserDetailsService, AuthService {

    private final HashMap<String, String> refreshStorage = new HashMap<>();

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final MapperUtils mapperUtils;

    /**
     * Метод позволяет зарегистрировать нового пользователя
     *
     * @param signUpRequestDTO принимает в качестве параметра SignUpRequestDTO
     */
    @Override
    @Transactional
    public ResponseEntity<String> signUp(SignUpRequestDTO signUpRequestDTO) {
        if (accountRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
            throw new ErrorTemplate(HttpStatus.NOT_FOUND, "E-mail занят!");
        }

        Account account = Account.builder()
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .userName(signUpRequestDTO.getUserName())
                .build();

        accountRepository.save(account);

        return new ResponseEntity<>("Пользователь зарегистрирован", HttpStatus.OK);
    }

    /**
     * Метод позволяет аутентифицировать и авторизовать пользователя
     *
     * @param email    email пользователя
     * @param password пароль пользователя
     */
    @Override
    public JwtResponseDTO signIn(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException ex) {
            throw new ErrorTemplate(HttpStatus.NOT_FOUND, "Неверный логин или пароль!");
        }

        Account account = accountRepository.findByEmail(email).get();

        final String accessToken = jwtProvider.generateAccessToken(account);
        final String refreshToken = jwtProvider.generateRefreshToken(account);
        refreshStorage.put(account.getEmail(), refreshToken);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    /**
     * Метод позволяет обновить JWT токен
     *
     * @param refreshToken токен для обновления
     */
    @Override
    public JwtResponseDTO refreshJWTToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            Account account = mapperUtils.map(claims.get("user"), Account.class);
            String saveRefreshToken = refreshStorage.get(account.getEmail());
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                String accessToken = jwtProvider.generateAccessToken(account);
                String newRefreshToken = jwtProvider.generateRefreshToken(account);
                refreshStorage.put(account.getEmail(), newRefreshToken);
                return new JwtResponseDTO(accessToken, newRefreshToken);
            }
        }
        throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Невалидный JWT токен");
    }

    /**
     * Метод позволяет получить информацию об авторизованном пользователе
     *
     * @param authentication пользователь авторизованной сессии
     */
    @Override
    public Account getUserPrincipal(Authentication authentication) {
        return mapperUtils.map((UserPrincipal) authentication.getPrincipal(), Account.class);
    }

    /**
     * Метод позволяет проверить существует ли пользователь в базе данных
     *
     * @param id UUID пользователя
     */
    @Override
    public Optional<Account> findById(UUID id) {
        if (id != null) {
            Optional<Account> accountFind = accountRepository.findById(id);
            if (accountFind.isEmpty()) {
                throw new ErrorTemplate(HttpStatus.BAD_REQUEST, "Аккаунт с ID " + id + " не найден!");
            }
            return accountFind;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(login).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", login)));
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }
}
