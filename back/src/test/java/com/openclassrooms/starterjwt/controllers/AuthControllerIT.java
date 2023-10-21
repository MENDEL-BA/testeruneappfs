package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JacksonTester<LoginRequest>  loginRequestJacksonTester;

    @Autowired
    private JacksonTester<SignupRequest> signupRequestJacksonTester;

    @Autowired
    private UserRepository userRepository;

    private static LoginRequest loginRequest;

    private static SignupRequest signupRequest;

    @BeforeAll
    static void init(){
        loginRequest = new LoginRequest();
        loginRequest.setEmail("user@yopmail.com");
        //loginRequest.setPassword("passer");
        signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("passer@123");
        signupRequest.setEmail("johndoe@yopmail.com");
    }

    @Test
    @DisplayName("Test authentification user")
    void testAuthenticatedUser() throws Exception{
        loginRequest.setPassword("passer");

        String jsonLogin = loginRequestJacksonTester.write(loginRequest).getJson();

        MockHttpServletResponse mockHttpServletResponse = mockMvc
                .perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonLogin))
                .andExpect(status().isOk()).andReturn().getResponse();

        String token = mockHttpServletResponse.getContentAsString();
        Assertions.assertNotNull(token);

    }

    @Test
    @DisplayName("Test authentification user avec mauvais identifiants")
    void testAuthenticatedUserWithBadCredential() throws Exception{
        loginRequest.setPassword("passer@123");
        String jsonLoginBad = loginRequestJacksonTester.write(loginRequest).getJson();

         mockMvc.perform(post("/api/auth/login")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonLoginBad))
                 .andExpect(status().isUnauthorized()).andReturn();

    }

    @Test
    @DisplayName("Test register user")
    void testregisterUser() throws Exception{
        String jsonRegister = signupRequestJacksonTester.write(signupRequest).getJson();

        mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegister))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully!"))).andReturn();

    }

    @Test
    @DisplayName("Test register user exist")
    void registerUserExist() throws Exception {
        String json = signupRequestJacksonTester.write(signupRequest).getJson();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error: Email is already taken!"))).andReturn();
    }

    @AfterAll
    void end() {
        String email = "johndoe@yopmail.com";
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email).get();
            userRepository.deleteById(user.getId());
        }
    }
}
