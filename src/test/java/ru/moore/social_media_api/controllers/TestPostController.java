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
import ru.moore.social_media_api.DTO.PostDTO;
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
public class TestPostController {

    @Autowired
    private AuthController authController;

    @Autowired
    private PostController postController;

    @Autowired
    private JwtProvider jwtProvider;

    static String accessToken;

    static ResponseEntity<PostDTO> post;

    @Test
    @Order(1)
    public void setUp() {
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUserName("TestUserName");
        signUpRequestDTO.setEmail("TestUserName@TestUserName.ru");
        signUpRequestDTO.setPassword("TestUserName");
        authController.signUp(signUpRequestDTO);

        SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("TestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("TestUserName");
        accessToken = authController.signIn(signInRequestDTO).getBody().getAccessToken();
    }

    @Test
    @Order(2)
    public void newPost() {
        PostDTO postDTO = new PostDTO();
        postDTO.setHeaderPost("setHeaderPost");
        postDTO.setTextPost("setTextPost");

        post = postController.newPost(null, postDTO, getAuthentication());

        Assertions.assertEquals(post.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void updatePost() {
        post.getBody().setHeaderPost("setHeaderPostUPDATE");
        post.getBody().setTextPost("setTextPostUPDATE");

        post = postController.updatePost(post.getBody(), getAuthentication());

        Assertions.assertEquals(post.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(4)
    public void deletePost() {
        Assertions.assertEquals(postController.deletePost(post.getBody(), getAuthentication()).getStatusCode(), HttpStatus.OK);
    }

    public Authentication getAuthentication() {
        Claims claims = jwtProvider.getAccessClaims(accessToken);
        ObjectMapper mapper = new ObjectMapper();
        UserPrincipal user = mapper.convertValue(claims.get("user"), UserPrincipal.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

}
