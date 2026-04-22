package org.week04lab01.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.student.domain.Student;
import org.week04lab01.student.domain.StudentService;
import org.week04lab01.student.infrastructure.StudentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student sampleStudent;

    @BeforeEach
    void setUp() {
        sampleStudent = new Student(
                "María",
                "García",
                "maria@uni.edu",
                LocalDate.of(2000, 5, 15),
                new ArrayList<>()
        );
        sampleStudent.setId(1L);
    }

    @Test
    void createStudent_shouldReturnSavedStudent_whenValidInput() {
        when(studentRepository.save(any(Student.class))).thenReturn(sampleStudent);

        Student result = studentService.createStudent(sampleStudent);

        assertNotNull(result);
        assertEquals("María", result.getFirstName());
        assertEquals("maria@uni.edu", result.getEmail());
        verify(studentRepository, times(1)).save(sampleStudent);
    }

    @Test
    void getStudent_shouldReturnStudent_whenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(sampleStudent));

        Student result = studentService.getStudent(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("María", result.getFirstName());
    }

    @Test
    void getStudent_shouldReturnNull_whenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Student result = studentService.getStudent(99L);

        assertNull(result);
    }

    @Test
    void deleteStudent_shouldCallDeleteById() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent_whenStudentExists() throws ResourceNotFoundException {
        Student updatedData = new Student(
                "María José",
                "García",
                "mariajose@uni.edu",
                LocalDate.of(2000, 5, 15),
                new ArrayList<>()
        );

        when(studentRepository.findById(1L)).thenReturn(Optional.of(sampleStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(sampleStudent);

        Student result = studentService.updateStudent(1L, updatedData);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(sampleStudent);
    }

    @Test
    void updateStudent_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> studentService.updateStudent(99L, sampleStudent));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        List<Student> students = List.of(
                sampleStudent,
                new Student("Pedro", "Ruiz", "pedro@uni.edu", LocalDate.of(1999, 3, 10), new ArrayList<>())
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getStudentsByCourse_shouldReturnStudents_forGivenCourseId() {
        List<Student> students = List.of(sampleStudent);
        when(studentRepository.findByCoursesId(1L)).thenReturn(students);

        List<Student> result = studentService.getStudentsByCourse(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("María", result.get(0).getFirstName());
    }

    @Test
    void getStudentsByCourse_shouldReturnEmptyList_whenNoneEnrolled() {
        when(studentRepository.findByCoursesId(99L)).thenReturn(new ArrayList<>());

        List<Student> result = studentService.getStudentsByCourse(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
