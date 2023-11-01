package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Test find User par son ID")
    void testFindUserByIDTest(){
        doReturn(Optional.of(new User())).when(userRepository).findById(anyLong());
        User User = userService.findById(1L);
        Assertions.assertNotNull(User);
    }

    @Test
    @DisplayName("Test find null User")
    void testFindNullUserByIDTest(){
        doReturn(Optional.empty()).when(userRepository).findById(anyLong());
        User User = userService.findById(1L);
        Assertions.assertNull(User);
    }

    @Test
    @DisplayName("Test delete user by user Id is null")
    void testDeleteUserIdNullTest() {
        doThrow(IllegalArgumentException.class).when(userRepository).deleteById(null);

        Throwable exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    userService.delete(null);
                });

        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test delete user")
    void testDeleteUserTest() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
