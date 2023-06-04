package ru.moore.social_media_api.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import ru.moore.social_media_api.DTO.auth.SignInRequestDTO;
import ru.moore.social_media_api.DTO.auth.SignUpRequestDTO;
import ru.moore.social_media_api.SocialMediaApiApplication;

@SpringBootTest(classes = SocialMediaApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class TestAuthController {

    @Autowired
    private AuthController authController;

    @Test
    @Order(1)
    public void signUpTest() {
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUserName("TestUserName");
        signUpRequestDTO.setEmail("TestUserName@TestUserName.ru");
        signUpRequestDTO.setPassword("TestUserName");

        Assertions.assertEquals(authController.signUp(signUpRequestDTO).getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(2)
    public void signInTest() {
        SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
        signInRequestDTO.setEmail("TestUserName@TestUserName.ru");
        signInRequestDTO.setPassword("TestUserName");

        Assertions.assertEquals(authController.signIn(signInRequestDTO).getStatusCode(), HttpStatus.OK);
    }

}
