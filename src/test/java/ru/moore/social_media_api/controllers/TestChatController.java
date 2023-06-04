package ru.moore.social_media_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import ru.moore.social_media_api.DTO.ChatDTO;
import ru.moore.social_media_api.DTO.RequestFriendshipDTO;
import ru.moore.social_media_api.DTO.auth.SignInRequestDTO;
import ru.moore.social_media_api.DTO.auth.SignUpRequestDTO;
import ru.moore.social_media_api.SocialMediaApiApplication;
import ru.moore.social_media_api.security.JwtProvider;
import ru.moore.social_media_api.security.UserPrincipal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = SocialMediaApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class TestChatController {

    @Autowired
    private AuthController authController;

    @Autowired
    private RequestFriendshipController requestFriendshipController;

    @Autowired
    private ChatController chatController;

    @Autowired
    private JwtProvider jwtProvider;

    static String accessTokenOneUser;

    static String accessTokenTwoUser;

    @Test
    @Order(1)
    public void setUp() {
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUserName("OneTestUserName");
        signUpRequestDTO.setEmail("OneTestUserName@TestUserName.ru");
        signUpRequestDTO.setPassword("OneTestUserName");
        authController.signUp(signUpRequestDTO);

        signUpRequestDTO.setUserName("TwoTestUserName");
        signUpRequestDTO.setEmail("TwoTestUserName@TestUserName.ru");
        signUpRequestDTO.setPassword("TwoTestUserName");
        authController.signUp(signUpRequestDTO);

        SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("OneTestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("OneTestUserName");
        accessTokenTwoUser = authController.signIn(signInRequestDTO).getBody().getAccessToken();

        signInRequestDTO.setEmail("TwoTestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("TwoTestUserName");
        accessTokenOneUser = authController.signIn(signInRequestDTO).getBody().getAccessToken();

        ResponseEntity<RequestFriendshipDTO> requestFriendshipDTOResponseEntity = requestFriendshipController.newRequest(getUserPrincipal(accessTokenOneUser).getId(), getAuthentication(accessTokenTwoUser));

        requestFriendshipController.confirmRequest(requestFriendshipDTOResponseEntity.getBody().getId(), getAuthentication(accessTokenOneUser));
    }

    @Test
    @Order(2)
    public void newMessage() {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setMessage("TestMessage");
        chatDTO.setAccountToId(getUserPrincipal(accessTokenTwoUser).getId());

        ResponseEntity<String> stringResponseEntity = chatController.newMessage(chatDTO, getAuthentication(accessTokenOneUser));

        Assertions.assertEquals(stringResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void getAllChatByFriendId() {
        UUID friendToId = getUserPrincipal(accessTokenTwoUser).getId();

        Assertions.assertEquals(chatController.getAllChatByFriendId(friendToId, getAuthentication(accessTokenOneUser)).getStatusCode(), HttpStatus.OK);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = jwtProvider.getAccessClaims(accessToken);
        ObjectMapper mapper = new ObjectMapper();
        UserPrincipal user = mapper.convertValue(claims.get("user"), UserPrincipal.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    public UserPrincipal getUserPrincipal(String accessToken) {
        Claims claims = jwtProvider.getAccessClaims(accessToken);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(claims.get("user"), UserPrincipal.class);
    }

}
