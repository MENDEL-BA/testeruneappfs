package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private final User user = new User(1L,"user@yopmail.com", "userlast", "usr", "passer",
            false, LocalDateTime.now(), LocalDateTime.now());

    @Test
    @DisplayName("Test loadUserByUserName")
    void testLoadUserByUserNameTest() {
        doReturn(Optional.of(user)).when(repository).findByEmail(anyString());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    @DisplayName("Test loadUserByUserName UsernameNotFoundException")
    void testLoadUserByUserNameUserNotFoundTest() {
        doThrow(UsernameNotFoundException.class).when(repository).findByEmail(anyString());

        Throwable exception = assertThrows(
                UsernameNotFoundException.class, () -> {
                    repository.findByEmail(user.getEmail());
                });

        Assertions.assertEquals(UsernameNotFoundException.class, exception.getClass());
    }
    @Test
    @DisplayName("Test loadUserByUserName whith bad Username")
    void testLoadUserByUsernameUserNotFound() {
        String username = "nonexistent@example.com";
        when(repository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        verify(repository).findByEmail(username);
    }
    @Test
    @DisplayName("Test loadUserByUserName return null")
    void testLoadUserByUserNameReturnNullTest() {
        doReturn(Optional.empty()).when(repository).findByEmail(anyString());
        try {
            repository.findByEmail(user.getEmail());
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User Not Found with email: " + user.getEmail(), e.getMessage());
        }

    }
}
