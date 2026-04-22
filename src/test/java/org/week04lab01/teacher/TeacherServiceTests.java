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

    @Test
    void createTeacher_shouldReturnSavedTeacher_whenValidInput() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(sampleTeacher);

        Teacher result = teacherService.createTeacher(sampleTeacher);

        assertNotNull(result);
        assertEquals("Roberto", result.getFirstName());
        assertEquals("roberto@uni.edu", result.getEmail());
        verify(teacherRepository, times(1)).save(sampleTeacher);
    }

    @Test
    void getTeacherById_shouldReturnTeacher_whenTeacherExists() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(sampleTeacher));

        Teacher result = teacherService.getTeacherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Roberto", result.getFirstName());
    }

    @Test
    void getTeacherById_shouldReturnNull_whenTeacherNotFound() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        Teacher result = teacherService.getTeacherById(99L);

        assertNull(result);
    }

    @Test
    void getAllTeachers_shouldReturnAllTeachers() {
        List<Teacher> teachers = List.of(
                sampleTeacher,
                new Teacher("Lucía", "Vargas", 55.0, "lucia@uni.edu", LocalDate.of(1975, 3, 12))
        );
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.getAllTeachers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteTeacher_shouldReturnTrue_whenTeacherExists() {
        when(teacherRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teacherRepository).deleteById(1L);

        boolean result = teacherService.deleteTeacher(1L);

        assertTrue(result);
        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTeacher_shouldReturnFalse_whenTeacherNotFound() {
        when(teacherRepository.existsById(99L)).thenReturn(false);

        boolean result = teacherService.deleteTeacher(99L);

        assertFalse(result);
        verify(teacherRepository, never()).deleteById(any());
    }

    @Test
    void updateTeacher_shouldReturnUpdatedTeacher_whenTeacherExists() {
        Teacher updatedData = new Teacher("Roberto Carlos", "Mendoza", 60.0, "robertoc@uni.edu", LocalDate.of(1980, 8, 20));
        updatedData.setCourses(new ArrayList<>());

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(sampleTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(sampleTeacher);

        Teacher result = teacherService.updateTeacher(1L, updatedData);

        assertNotNull(result);
        verify(teacherRepository, times(1)).save(sampleTeacher);
    }

    @Test
    void updateTeacher_shouldReturnNull_whenTeacherNotFound() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        Teacher result = teacherService.updateTeacher(99L, sampleTeacher);

        assertNull(result);
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void getTeacherByEmail_shouldReturnTeacher_whenEmailExists() throws ResourceNotFoundException {
        when(teacherRepository.findByEmail("roberto@uni.edu")).thenReturn(Optional.of(sampleTeacher));

        Teacher result = teacherService.getTeacherByEmail("roberto@uni.edu");

        assertNotNull(result);
        assertEquals("roberto@uni.edu", result.getEmail());
    }

    @Test
    void getTeacherByEmail_shouldThrowResourceNotFoundException_whenEmailNotFound() {
        when(teacherRepository.findByEmail("noexiste@uni.edu")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByEmail("noexiste@uni.edu"));
    }

    @Test
    void getTeacherByFirstName_shouldReturnTeacher_whenFirstNameExists() throws ResourceNotFoundException {
        when(teacherRepository.findByFirstName("Roberto")).thenReturn(Optional.of(sampleTeacher));

        Teacher result = teacherService.getTeacherByFirstName("Roberto");

        assertNotNull(result);
        assertEquals("Roberto", result.getFirstName());
    }

    @Test
    void getTeacherByFirstName_shouldThrowResourceNotFoundException_whenFirstNameNotFound() {
        when(teacherRepository.findByFirstName("Desconocido")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByFirstName("Desconocido"));
    }

    @Test
    void getTeacherByLastName_shouldReturnTeacher_whenLastNameExists() throws ResourceNotFoundException {
        when(teacherRepository.findByLastName("Mendoza")).thenReturn(Optional.of(sampleTeacher));

        Teacher result = teacherService.getTeacherByLastName("Mendoza");

        assertNotNull(result);
        assertEquals("Mendoza", result.getLastName());
    }

    @Test
    void getTeacherByLastName_shouldThrowResourceNotFoundException_whenLastNameNotFound() {
        when(teacherRepository.findByLastName("Inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByLastName("Inexistente"));
    }

    @Test
    void getTeacherByBirthday_shouldReturnTeacher_whenBirthdayExists() throws ResourceNotFoundException {
        LocalDate birthday = LocalDate.of(1980, 8, 20);
        when(teacherRepository.findByBirthday(birthday)).thenReturn(Optional.of(sampleTeacher));

        Teacher result = teacherService.getTeacherByBirthday(birthday);

        assertNotNull(result);
        assertEquals(birthday, result.getBirthday());
    }

    @Test
    void getTeacherByBirthday_shouldThrowResourceNotFoundException_whenBirthdayNotFound() {
        LocalDate birthday = LocalDate.of(1900, 1, 1);
        when(teacherRepository.findByBirthday(birthday)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.getTeacherByBirthday(birthday));
    }
}
