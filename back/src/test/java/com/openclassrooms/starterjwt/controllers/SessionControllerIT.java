package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    public SessionDto sessionDto;

    @Autowired
    private JacksonTester<SessionDto> sessionDtoJacksonTester;

    @BeforeEach
    void init(){
        Long id = 1L;

        List<Long> userList = new ArrayList<>();
        userList.add(1L);
        sessionDto = new SessionDto(id, "Session", new Date(), 1L, "Session Started", userList,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Creation de Session")
    @WithMockUser("user@yopmail.com")
    void testCreateSession() throws Exception {
                mockMvc
                .perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Session"))).andReturn().getResponse();
    }

    /*@Test
    @DisplayName("Ajout de participant à une Session")
    @WithMockUser("user@yopmail.com")
    void testAddParticipateToSession() throws Exception {
        mockMvc
                .perform(post("/api/session/2/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }*/

    @Test
    @DisplayName("Ajout de participant à une Session sans se connecter")
    void testAddParticipateToSessionSansConeecter() throws Exception {
        mockMvc
                .perform(post("/api/session/2/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Recuperation de Session")
    @WithMockUser("user@yopmail.com")
    void testFindAllSession() throws Exception {
        mockMvc
                .perform(get("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(sessionDto.getName())))
                .andExpect(jsonPath("$[0].id", is(2))).andReturn().getResponse();

    }

    @Test
    @DisplayName("Recuperation de Session sans se connecter")
    void testFindAllSessionWithoutConnected() throws Exception {
        mockMvc
                .perform(get("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized()).andReturn().getResponse();

    }

    @Test
    @DisplayName("Recuperation d'une Session sans se connecter")
    void testFindOneSessionWithoutConnected() throws Exception {
        mockMvc
                .perform(get("/api/session/9900090")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized()).andReturn().getResponse();

    }

    @Test
    @DisplayName("Creation de Session Sans se connecter")
    void testCreateWhithoutConnectedSession() throws Exception {
        mockMvc
                .perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
    }

    @Test
    @DisplayName("Suppression de Session sans se connecter")
    //@WithMockUser("user@yopmail.com")
    void testDeletedSessionWhithoutConnection() throws Exception {
        mockMvc
                .perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized()).andReturn().getResponse();
    }

}