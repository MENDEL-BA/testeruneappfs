package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class AuthControllerTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Authentication authentication;

    @Autowired
    private AuthController authController;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private User user;
    private UserDetailsImpl userDetails;


    private final Long id = 1L;
    private final String email = "user@yopmail.com";
    private final String firstName = "user";
    private final String lastName = "userlast";
    private final String password = "passer";
    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2NzkzNDcxNDksImV4cCI" +
            "6MTY3OTQzMzU0OX0.LMLUeOwdXzY42N1AQRo2xPiBppbXmjSHDqS73EuxSb1zoOwjlRQiMt92COFPgb0_QbPk-nBCRDy075a9kA0Ptg";


    @BeforeEach
    void setup() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        signupRequest.setLastName(lastName);
        signupRequest.setFirstName(firstName);

        loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        user = new User(id, email, lastName, firstName, password, true, LocalDateTime.now(), LocalDateTime.now());
        userDetails = new UserDetailsImpl(id, email, firstName, lastName, true, password);
    }

    @Test
    @DisplayName("Test if user is authenticated")
    void testAuthenticatedUser(){
        doReturn(authentication).when(authenticationManager).authenticate(any());
        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(token).when(jwtUtils).generateJwtToken(authentication);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(any());

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();

        assert jwtResponse != null;

        Assertions.assertNotNull(jwtResponse.getToken());
        Assertions.assertEquals(lastName, jwtResponse.getLastName());
        Assertions.assertTrue(jwtResponse.getAdmin());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test register user ")
    void testRegisterUser(){
        doReturn(false).when(userRepository).existsByEmail(any());
        doAnswer(returnsFirstArg()).when(userRepository).save(any(User.class));
        doAnswer(returnsFirstArg()).when(passwordEncoder).encode(anyString());

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    @DisplayName("Test register user if exist")
    void testRegisterExistUser(){
        doReturn(true).when(userRepository).existsByEmail(any());
        doAnswer(returnsFirstArg()).when(userRepository).save(any(User.class));
        doAnswer(returnsFirstArg()).when(passwordEncoder).encode(anyString());
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Error: Email is already taken!", ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

}
