package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
class SessionControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private static SessionRepository sessionRepository;

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
    @Order(1)
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
    @Order(2)
    void testAddParticipateToSessionSansConeecter() throws Exception {
        mockMvc
                .perform(post("/api/session/2/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Recuperation de Session")
    @WithMockUser("user@yopmail.com")
    @Order(3)
    void testFindAllSession() throws Exception {
        mockMvc
                .perform(get("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(sessionDto.getName())))
                .andExpect(jsonPath("$[0].description", is(sessionDto.getDescription()))).andReturn().getResponse();

    }

    @Test
    @DisplayName("Recuperation de Session sans se connecter")
    @Order(4)
    void testFindAllSessionWithoutConnected() throws Exception {
        mockMvc
                .perform(get("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized()).andReturn().getResponse();

    }

    @Test
    @DisplayName("Recuperation d'une Session sans se connecter")
    @Order(5)
    void testFindOneSessionWithoutConnected() throws Exception {
        mockMvc
                .perform(get("/api/session/9900090")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized()).andReturn().getResponse();

    }

    @Test
    @DisplayName("Creation de Session Sans se connecter")
    @Order(6)
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
    @Order(7)
    void testDeletedSessionWhithoutConnection() throws Exception {
        mockMvc
                .perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJacksonTester.write(sessionDto).getJson()))
                .andExpect(status().isUnauthorized()).andReturn().getResponse();
    }


    @AfterAll
    static void deleteAll(){
        sessionRepository.deleteAll();
    }

}