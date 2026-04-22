package org.week04lab01.teacher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.teacher.domain.Teacher;
import org.week04lab01.teacher.domain.TeacherService;
import org.week04lab01.teacher.infrastructure.TeacherRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher sampleTeacher;

    @BeforeEach
    void setUp() {
        sampleTeacher = new Teacher(
                "Roberto",
                "Mendoza",
                45.0,
                "roberto@uni.edu",
                LocalDate.of(1980, 8, 20)
        );
        sampleTeacher.setId(1L);
        sampleTeacher.setCourses(new ArrayList<>());
    }

    // -------------------------------------------------------------------------
    // createTeacher
    // -------------------------------------------------------------------------

    @Test
    void createTeacher_shouldReturnSavedTeacher_whenValidInput() {
        // Arrange
        when(teacherRepository.save(any(Teacher.class))).thenReturn(sampleTeacher);

        // Act
        Teacher result = teacherService.createTeacher(sampleTeacher);

        // Assert
        assertNotNull(result);
        assertEquals("Roberto", result.getFirstName());
        assertEquals("roberto@uni.edu", result.getEmail());
        verify(teacherRepository, times(1)).save(sampleTeacher);
    }

    // -------------------------------------------------------------------------
    // getTeacherById
    // -------------------------------------------------------------------------

    @Test
    void getTeacherById_shouldReturnTeacher_whenTeacherExists() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(sampleTeacher));

        // Act
        Teacher result = teacherService.getTeacherById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Roberto", result.getFirstName());
    }

    @Test
    void getTeacherById_shouldReturnNull_whenTeacherNotFound() {
        // Arrange
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.getTeacherById(99L);

        // Assert
        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // getAllTeachers
    // -------------------------------------------------------------------------

    @Test
    void getAllTeachers_shouldReturnAllTeachers() {
        // Arrange
        List<Teacher> teachers = List.of(
                sampleTeacher,
                new Teacher("Lucía", "Vargas", 55.0, "lucia@uni.edu", LocalDate.of(1975, 3, 12))
        );
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Act
        List<Teacher> result = teacherService.getAllTeachers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // -------------------------------------------------------------------------
    // deleteTeacher
    // -------------------------------------------------------------------------

    @Test
    void deleteTeacher_shouldReturnTrue_whenTeacherExists() {
        // Arrange
        when(teacherRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teacherRepository).deleteById(1L);

        // Act
        boolean result = teacherService.deleteTeacher(1L);

        // Assert
        assertTrue(result);
        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTeacher_shouldReturnFalse_whenTeacherNotFound() {
        // Arrange
        when(teacherRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = teacherService.deleteTeacher(99L);

        // Assert
        assertFalse(result);
        verify(teacherRepository, never()).deleteById(any());
    }

    // -------------------------------------------------------------------------
    // updateTeacher
    // -------------------------------------------------------------------------

    @Test
    void updateTeacher_shouldReturnUpdatedTeacher_whenTeacherExists() {
        // Arrange
        Teacher updatedData = new Teacher("Roberto Carlos", "Mendoza", 60.0, "robertoc@uni.edu", LocalDate.of(1980, 8, 20));
        updatedData.setCourses(new ArrayList<>());

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(sampleTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(sampleTeacher);

        // Act
        Teacher result = teacherService.updateTeacher(1L, updatedData);

        // Assert
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(sampleTeacher);
    }

    @Test
    void updateTeacher_shouldReturnNull_whenTeacherNotFound() {
        // Arrange
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.updateTeacher(99L, sampleTeacher);

        // Assert
        assertNull(result);
        verify(teacherRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // getTeacherByEmail
    // -------------------------------------------------------------------------

    @Test
    void getTeacherByEmail_shouldReturnTeacher_whenEmailExists() throws ResourceNotFoundException {
        // Arrange
        when(teacherRepository.findByEmail("roberto@uni.edu")).thenReturn(Optional.of(sampleTeacher));

        // Act
        Teacher result = teacherService.getTeacherByEmail("roberto@uni.edu");

        // Assert
        assertNotNull(result);
        assertEquals("roberto@uni.edu", result.getEmail());
    }

    @Test
    void getTeacherByEmail_shouldThrowResourceNotFoundException_whenEmailNotFound() {
        // Arrange
        when(teacherRepository.findByEmail("noexiste@uni.edu")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByEmail("noexiste@uni.edu"));
    }

    // -------------------------------------------------------------------------
    // getTeacherByFirstName
    // -------------------------------------------------------------------------

    @Test
    void getTeacherByFirstName_shouldReturnTeacher_whenFirstNameExists() throws ResourceNotFoundException {
        // Arrange
        when(teacherRepository.findByFirstName("Roberto")).thenReturn(Optional.of(sampleTeacher));

        // Act
        Teacher result = teacherService.getTeacherByFirstName("Roberto");

        // Assert
        assertNotNull(result);
        assertEquals("Roberto", result.getFirstName());
    }

    @Test
    void getTeacherByFirstName_shouldThrowResourceNotFoundException_whenFirstNameNotFound() {
        // Arrange
        when(teacherRepository.findByFirstName("Desconocido")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByFirstName("Desconocido"));
    }

    // -------------------------------------------------------------------------
    // getTeacherByLastName
    // -------------------------------------------------------------------------

    @Test
    void getTeacherByLastName_shouldReturnTeacher_whenLastNameExists() throws ResourceNotFoundException {
        // Arrange
        when(teacherRepository.findByLastName("Mendoza")).thenReturn(Optional.of(sampleTeacher));

        // Act
        Teacher result = teacherService.getTeacherByLastName("Mendoza");

        // Assert
        assertNotNull(result);
        assertEquals("Mendoza", result.getLastName());
    }

    @Test
    void getTeacherByLastName_shouldThrowResourceNotFoundException_whenLastNameNotFound() {
        // Arrange
        when(teacherRepository.findByLastName("Inexistente")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByLastName("Inexistente"));
    }

    // -------------------------------------------------------------------------
    // getTeacherByBirthday
    // -------------------------------------------------------------------------

    @Test
    void getTeacherByBirthday_shouldReturnTeacher_whenBirthdayExists() throws ResourceNotFoundException {
        // Arrange
        LocalDate birthday = LocalDate.of(1980, 8, 20);
        when(teacherRepository.findByBirthday(birthday)).thenReturn(Optional.of(sampleTeacher));

        // Act
        Teacher result = teacherService.getTeacherByBirthday(birthday);

        // Assert
        assertNotNull(result);
        assertEquals(birthday, result.getBirthday());
    }

    @Test
    void getTeacherByBirthday_shouldThrowResourceNotFoundException_whenBirthdayNotFound() {
        // Arrange
        LocalDate birthday = LocalDate.of(1900, 1, 1);
        when(teacherRepository.findByBirthday(birthday)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByBirthday(birthday));
    }
}
