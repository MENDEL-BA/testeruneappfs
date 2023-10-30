package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SessionControllerTest {

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @Autowired
    private SessionController sessionController;

    @Autowired
    private SessionMapper sessionMapper;

    private Session session;

    @BeforeEach
    void start() {
        Long id = 1L;
        String email = "user@yopmail.com";
        String firstName = "user";
        String lastName = "userlast";
        String password = "passer";
        User user1 = new User(id, email, lastName, firstName, password, false, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(id, email, lastName, firstName, password, false, LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher = new Teacher(id, lastName, firstName,LocalDateTime.now(),LocalDateTime.now());
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        session = new Session(id, "Session", new Date(), "description", teacher, userList,
                LocalDateTime.now(), LocalDateTime.now());
    }


    @Test
    @DisplayName("Test recherche de session par son ID")
    void testFindById() {
        doReturn(session).when(sessionService).getById(any());
        ResponseEntity<?> response = sessionController.findById("1");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Recuperation de tous les sessions")
    void testFindAllSession(){
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);
        sessionList.add(session);
        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionMapper.toDto(session));
        sessionDtoList.add(sessionMapper.toDto(session));
        doReturn(sessionList).when(sessionService).findAll();

        ResponseEntity<?> response = sessionController.findAll();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals( response.getBody(), sessionDtoList);

    }

    @Test
    @DisplayName("Creation de session")
    void testCreationSession(){
        when(sessionService.create(any(Session.class))).thenReturn(session);

        ResponseEntity<?> response = sessionController.create(sessionMapper.toDto(session));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), sessionMapper.toDto(session));

    }



    @Test
    @DisplayName("Test update avec une mauvaise parametre")
    void testUpdateSessionNumberFormatException() {
        doThrow(NumberFormatException.class).when(sessionService).update(anyLong(), any(Session.class));
        ResponseEntity<?> response = null;
        try {
            response = sessionController.update("NaN", sessionMapper.toDto(session));
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Recuperer une session par son id")
    void testFindSessionByID(){
        doReturn(session).when(sessionService).getById(anyLong());
        ResponseEntity<?> response = sessionController.findById("1");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @DisplayName("Recuperer une session nulle")
    void testFindSessionNullByID(){
        doReturn(null).when(sessionService).getById(anyLong());
        ResponseEntity<?> response = sessionController.findById("1");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Recuperer une session avec une mauvaise parametre")
    void testFindSessionByBadID(){
        doThrow(NumberFormatException.class).when(sessionService).getById(anyLong());
        ResponseEntity<?> response = null;
        try {
             response = sessionController.findById("NaN");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

    }

    @Test
    @DisplayName("Ajout d'un(e) participant(e) une session")
    void testAddParticipanteToSession(){
        doNothing().when(sessionService).participate(anyLong(), anyLong());
        ResponseEntity<?> response = null;
        try {
            response = sessionController.participate(String.valueOf(1),String.valueOf(1));
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        }

    }

    @Test
    @DisplayName("Ajout d'un(e) participant(e) une session avec des mauvaises parametres")
    void testAddParticipanteToSessionWithBadParameter(){
        doNothing().when(sessionService).participate(anyLong(), anyLong());
        ResponseEntity<?> response = null;
        try {
            response = sessionController.participate("NaN","NaN");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

    }

    @Test
    @DisplayName("Test absense de participant à une session avec des mauvaises parametres")
    void testNoParticipanteToSessionWithBadParameter(){
        doNothing().when(sessionService).participate(anyLong(), anyLong());
        ResponseEntity<?> response = sessionController.noLongerParticipate("NaN","NaN");
        verify(sessionService, times(0)).noLongerParticipate(anyLong(), anyLong());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("Test de participant à une session")
    void testNumberParticipanteToSession(){
        doNothing().when(sessionService).participate(anyLong(), anyLong());
        ResponseEntity<?> response = sessionController.noLongerParticipate(String.valueOf(1),String.valueOf(1));
        verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("Delete  session")
    void testDeleteSession(){
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save(String.valueOf(1L));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("Delete session avec un mauvais parametre")
    void testDeleteSessionWithBadParameter(){
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("NaN");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("Delete session is not exist")
    void testDeleteSessionIsNotExist(){
        when(sessionService.getById(anyLong())).thenReturn(null);

        ResponseEntity<?> response = sessionController.save(String.valueOf(anyLong()));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
}