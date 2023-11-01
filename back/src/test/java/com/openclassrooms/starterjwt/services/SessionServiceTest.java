package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SessionServiceTest {

    private User user;
    private Session session;

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        User user1 = new User(1L,"user@user.com", "User", "User",
                "passer123",
                false, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(2L,"user2@user.com", "User2",
                "User2", "passer123",
                true, LocalDateTime.now(), LocalDateTime.now());
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        session = new Session(1L,"Session1",new Date(),"Description", new Teacher(), userList,
                LocalDateTime.now(), LocalDateTime.now());
        user = new User();
        user.setId(12L);
    }

    @Test
    @DisplayName("Test de creation de session")
    void testCreationSessionTest(){
        doReturn(session).when(sessionRepository).save(any());

        Session response = sessionService.create(session);
        Assertions.assertNotNull(response, "La session creee ne peut pas etre null");
        Assertions.assertEquals(session, response);
    }

    @Test
    @DisplayName("Test de update de session")
    void testUpdateSessionTest(){
        doReturn(session).when(sessionRepository).save(any());

        Session response = sessionService.update(1L,session);
        Assertions.assertNotNull(response, "La session creee ne peut pas etre null");
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("Test de recuperation de session par son ID")
    void testGetSessionByIDTest(){
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());

        Session response = sessionService.getById(1L);
        Assertions.assertNotNull(response, "La session creee ne peut pas etre null");
        Assertions.assertEquals(session, response);
    }

    @Test
    @DisplayName("est de recuperation de session par un ID null")
    void testGetSessionByNullIDTest(){
        doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());

        Session response = sessionService.getById(1L);
        Assertions.assertNull(response);
    }

    @Test
    @DisplayName("Test de suppression de session par son ID")
    void testDeleteSessionByIDTest(){
        sessionRepository.deleteById(1L);
        verify(sessionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test de suppression de session par un ID Nulle")
    void testDeleteSessionByNullIDTest(){
        doThrow(IllegalArgumentException.class).when(sessionRepository).deleteById(null);
        Throwable throwable = Assertions.assertThrows(
                IllegalArgumentException.class, ()-> sessionRepository.deleteById(null));

        Assertions.assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

    @Test
    @DisplayName("Test de recuperation de tous les sessions")
    void testFindAllSessionTest(){
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);
        doReturn(sessionList).when(sessionRepository).findAll();
        List<Session> sessions = sessionService.findAll();
        Assertions.assertEquals(sessions, sessionList);
    }

    @Test
    @DisplayName("Test d'ajout de participant a une session")
    void testAddParticipateToSessionTest(){
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());

        sessionService.participate(1L, 12L);
        verify(sessionRepository,times(1)).findById(anyLong());
        verify(userRepository,times(1)).findById(anyLong());
        verify(sessionRepository,times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("Test d'ajout de participant a une session avec des params nulls")
    void testAddParticipateToSessionWithParamIsNullTest(){
        doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());
        doReturn(Optional.empty()).when(userRepository).findById(anyLong());
        Throwable throwable = Assertions.assertThrows(
                NotFoundException.class, ()-> sessionService.participate(1L, 12L));
        Assertions.assertEquals(NotFoundException.class, throwable.getClass());
    }

    @Test
    @DisplayName("Test noLongerParticipate a une session")
    void testNoLongerParticipateTest(){
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
        sessionService.noLongerParticipate(1L, 1L);
        verify(sessionRepository,times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("Test noLongerParticipate with BadRequestException")
    void testNoLongerParticipateBadRequestTest() {
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
        Throwable exception = Assertions.assertThrows(
                BadRequestException.class, () -> {
                    sessionService.noLongerParticipate(1L, 12L);
                });

        Assertions.assertEquals(BadRequestException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test noLongerParticipate throw NoFoundException")
    void testNoLongerParticipateNotFoundToSessionTest() {
        doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());
        Throwable exception = Assertions.assertThrows(
                NotFoundException.class, () -> {
                    sessionService.noLongerParticipate(1L, 1L);
                });

        Assertions.assertEquals(NotFoundException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test delete session")
    void testDeleteSessionTest() {
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test delete session by user Id is null")
    void testDeleteUserIdNullTest() {
        doThrow(IllegalArgumentException.class).when(sessionRepository).deleteById(null);

        Throwable exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    sessionService.delete(null);
                });

        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
    }


}
