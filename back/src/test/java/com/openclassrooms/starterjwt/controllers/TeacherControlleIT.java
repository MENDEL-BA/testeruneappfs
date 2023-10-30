package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeacherControlleIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Test Recuperation de tous les teachers sans se connecter")
    void tesFindAllSansConnectionTeacherIT() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/teacher/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test Recuperation de tous les teachers")
    @WithMockUser("user@yopmail.com")
    void tesFindAllTeacherIT() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/teacher/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John"))).andReturn().getResponse();
    }

    @Test
    @DisplayName("Test Recuperation de teacher par son ID")
    @WithMockUser("user@yopmail.com")
    void tesFindTeacherByIdIT() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName", is("John"))).andReturn().getResponse();
    }

    @Test
    @DisplayName("Test Recuperation de teacher par son ID sans se connecter")
    void tesFindTeacherByIdSansConnectionIT() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
