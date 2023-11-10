package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SessionMapperTest {

    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);
    private Session session;
    private User user1;

    @Mock
    private SessionDto sessionDto1;

    @Mock
    private SessionDto sessionDto2;
    @BeforeEach
    void init(){
        session = new Session();
        session.setDescription("Test session");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        user1 = new User();
        user1.setId(101L);

        User user2 = new User();
        user2.setId(102L);

        session.setUsers(Collections.singletonList(user1));

        sessionDto1 = new SessionDto(1L,"test",new Date(),1L,
                "desc", null,null, null);
    }

    @Test
    @DisplayName("Return a valid session DTO")
    void testToDto() {
        SessionDto sessionDto = sessionMapper.toDto(session);

        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(Collections.singletonList(user1.getId()), sessionDto.getUsers());
    }
}
