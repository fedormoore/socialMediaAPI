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
import ru.moore.social_media_api.DTO.RequestFriendshipDTO;
import ru.moore.social_media_api.DTO.auth.SignInRequestDTO;
import ru.moore.social_media_api.DTO.auth.SignUpRequestDTO;
import ru.moore.social_media_api.SocialMediaApiApplication;
import ru.moore.social_media_api.security.JwtProvider;
import ru.moore.social_media_api.security.UserPrincipal;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SocialMediaApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class TestRequestFriendshipController {

    @Autowired
    private AuthController authController;

    @Autowired
    private RequestFriendshipController requestFriendshipController;

    @Autowired
    private JwtProvider jwtProvider;

    static String accessTokenOneUser;

    static String accessTokenTwoUser;

    static String accessTokenThree;

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

        signUpRequestDTO.setUserName("ThreeTestUserName");
        signUpRequestDTO.setEmail("ThreeTestUserName@TestUserName.ru");
        signUpRequestDTO.setPassword("ThreeTestUserName");
        authController.signUp(signUpRequestDTO);

        SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("OneTestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("OneTestUserName");
        accessTokenTwoUser = authController.signIn(signInRequestDTO).getBody().getAccessToken();

        signInRequestDTO.setEmail("TwoTestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("TwoTestUserName");
        accessTokenOneUser = authController.signIn(signInRequestDTO).getBody().getAccessToken();

        signInRequestDTO.setEmail("ThreeTestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("ThreeTestUserName");
        accessTokenThree = authController.signIn(signInRequestDTO).getBody().getAccessToken();
    }

    @Test
    @Order(2)
    public void getAllRequest() {
        Assertions.assertEquals(requestFriendshipController.getAllRequest(getAuthentication(accessTokenOneUser)).getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void newRequest() {
        ResponseEntity<RequestFriendshipDTO> requestFriendshipDTOResponseEntity = requestFriendshipController.newRequest(getUserPrincipal(accessTokenTwoUser).getId(), getAuthentication(accessTokenOneUser));

        Assertions.assertEquals(requestFriendshipDTOResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(4)
    public void confirmRequest() {
        ResponseEntity<RequestFriendshipDTO> requestFriendshipDTOResponseEntity = requestFriendshipController.newRequest(getUserPrincipal(accessTokenTwoUser).getId(), getAuthentication(accessTokenOneUser));
        RequestFriendshipDTO requestFriendshipDTOResponse = requestFriendshipDTOResponseEntity.getBody();

        Assertions.assertEquals(requestFriendshipController.confirmRequest(requestFriendshipDTOResponse.getId(), getAuthentication(accessTokenTwoUser)).getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(5)
    public void rejectedRequest() {
        ResponseEntity<RequestFriendshipDTO> requestFriendshipDTOResponseEntity = requestFriendshipController.newRequest(getUserPrincipal(accessTokenTwoUser).getId(), getAuthentication(accessTokenOneUser));
        RequestFriendshipDTO requestFriendshipDTOResponse = requestFriendshipDTOResponseEntity.getBody();

        Assertions.assertEquals(requestFriendshipController.rejectedRequest(requestFriendshipDTOResponse.getId(), getAuthentication(accessTokenTwoUser)).getStatusCode(), HttpStatus.OK);
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
