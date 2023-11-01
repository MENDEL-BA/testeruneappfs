package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;
    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Test Find all Teacher")
    void testFindAllTeacherTest(){
        List<Teacher> teachers = Arrays.asList(new Teacher());
        doReturn(teachers).when(teacherRepository).findAll();
        List<Teacher> teacherList = teacherService.findAll();
        Assertions.assertNotNull(teacherList);
        Assertions.assertEquals(teachers, teacherList);
    }

    @Test
    @DisplayName("Test find Teacher par son ID")
    void testFindTeacherByIDTest(){
        doReturn(Optional.of(new Teacher())).when(teacherRepository).findById(anyLong());
        Teacher teacher = teacherService.findById(1L);
        Assertions.assertNotNull(teacher);
    }

    @Test
    @DisplayName("Test find null Teacher")
    void testFindNullTeacherByIDTest(){
        doReturn(Optional.empty()).when(teacherRepository).findById(anyLong());
        Teacher teacher = teacherService.findById(1L);
        Assertions.assertNull(teacher);
    }

}
