package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIT {

    @Autowired
    MockMvc mockMvc;
    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @Autowired
    private JacksonTester<SignupRequest> signupRequestJacksonTester;

    @Autowired
    private JacksonTester<LoginRequest> loginRequestJacksonTester;

    @Autowired
    private JacksonTester<User> userJacksonTester;

    @BeforeAll
    void start() throws IOException {
        signupRequest = new SignupRequest();
        signupRequest.setFirstName("Hann");
        signupRequest.setLastName("Solo");
        signupRequest.setPassword("passer123");
        signupRequest.setEmail("hann@solo.com");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("hann@solo.com");
        loginRequest.setPassword("passer123");
    }

    @Test
    @DisplayName("Test de recuperation de user par son ID")
    @WithMockUser("user@yopmail.com")
    void testFindUserByID() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is("user@yopmail.com")))
                .andExpect(jsonPath("$.id",
                        CoreMatchers.is(1))).andReturn();
    }

    @Test
    @DisplayName("Test de recuperation de user par son ID sans se connecter")
    void testFindUserByIDWithNotConnected() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", Matchers.is("Full authentication is required to access this resource"))).andReturn();
    }

    @Test
    @DisplayName("Test de save de user ")
    void testSaveUser() throws Exception {
        mockMvc.perform(post("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test user Find By Id ID not found")
    @WithMockUser(username = "user@yopmail.com")
    void testUserFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/1000"))
                .andExpect(status().isNotFound()).andReturn();
    }
}
