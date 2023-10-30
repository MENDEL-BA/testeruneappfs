package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class TeacherControllerTest {

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private TeacherController teacherController;

    @Autowired
    private TeacherMapper teacherMapper;

    private Teacher teacher;
    private Teacher teacher2;

    @BeforeEach
    void start() {
        teacher = new Teacher(1L, "John", "Doe", LocalDateTime.now(), LocalDateTime.now());
        teacher2 = new Teacher(2L, "Albert", "Einstein", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Test get Teacher By ID")
    void testFindById() {
        doReturn(teacher).when(teacherService).findById(anyLong());

        ResponseEntity<?> response = teacherController.findById("1");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test get Teacher By Bad ID")
    void testFindByBAdId() {
        doThrow(NumberFormatException.class).when(teacherService).findById(anyLong());

        ResponseEntity<?> response = null;
        try {
            response = teacherController.findById("NaN");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test get All Teacher")
    void testFinadAllTeacher() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);
        teacherList.add(teacher2);
        doReturn(teacherList).when(teacherService).findAll();
        ResponseEntity<?> response = teacherController.findAll();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), teacherMapper.toDto(teacherList));
    }

    @Test
    @DisplayName("Test get Teacher is not exist By ID")
    void testFindByIdTeacherNotExist() {
        doReturn(null).when(teacherService).findById(anyLong());

        ResponseEntity<?> response = teacherController.findById("1");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
