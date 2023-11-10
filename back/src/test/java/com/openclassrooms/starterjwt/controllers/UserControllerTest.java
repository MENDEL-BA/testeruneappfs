package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    private final Long id = 1L;
    private final String email = "user2@yopmail.com";
    private final String firstName = "user2";
    private final String lastName = "userlast2";
    private final String password = "passer123";

    private final User user = new User(id, email, lastName, firstName, password, true, LocalDateTime.now(), LocalDateTime.now());

    private final User user2 = new User(1L, email, lastName, firstName, password, true,LocalDateTime.now(), LocalDateTime.now());
    private final UserDetails userDetails = new UserDetailsImpl(id, email, firstName, lastName, true, password);


    @Test
    @DisplayName("Recuperation d'un user par son ID")
    void testFindUserById(){
        doReturn(user).when(userService).findById(anyLong());
        ResponseEntity<?> response = userController.findById("1");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Recuperation d'un user par un mauvais ID")
    void testFindUserByBadId(){
        doThrow(NumberFormatException.class).when(userService).findById(anyLong());
        ResponseEntity<?> response = null;
        try {
            response =  userController.findById("NaN");
        }catch (NumberFormatException e){
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Recuperation d'un user inexistant")
    void testFindUserByIdNotFound(){
        doReturn(null).when(userService).findById(anyLong());
        ResponseEntity<?> response = userController.findById("2");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    @DisplayName("Test delete user")
    void testSaveUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Test delete user not found")
    void testSaveUserNotFound() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userService.findById(Long.valueOf(id))).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test delete user whith not a number")
    void testSaveNumberFormatException() {
        ResponseEntity<?> responseEntity = userController.save("invalid");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
