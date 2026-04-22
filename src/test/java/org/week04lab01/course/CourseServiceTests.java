package org.week04lab01.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.week04lab01.course.domain.Course;
import org.week04lab01.course.domain.CourseService;
import org.week04lab01.course.dto.CourseRequestDto;
import org.week04lab01.course.dto.CourseResponseDto;
import org.week04lab01.course.infrastructure.CourseRepository;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.student.domain.Student;
import org.week04lab01.student.infrastructure.StudentRepository;
import org.week04lab01.teacher.domain.Teacher;
import org.week04lab01.teacher.infrastructure.TeacherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseService courseService;

    private Course sampleCourse;
    private CourseRequestDto sampleRequestDto;

    @BeforeEach
    void setUp() {
        sampleCourse = new Course("Algoritmos", 4, "Curso de algoritmos", 3);
        sampleCourse.setId(1L);
        sampleCourse.setStudents(new ArrayList<>());

        sampleRequestDto = new CourseRequestDto("Algoritmos", 4, "Curso de algoritmos", 3);
    }

    @Test
    void createCourse_shouldReturnCourseResponseDto_whenValidInput() {

        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);

        CourseResponseDto result = courseService.createCourse(sampleRequestDto);

        assertNotNull(result);
        assertEquals("Algoritmos", result.getName());
        assertEquals(4, result.getCreditNumber());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void getCourse_shouldReturnCourseResponseDto_whenCourseExists() throws ResourceNotFoundException {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));

        CourseResponseDto result = courseService.getCourse(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Algoritmos", result.getName());
    }

    @Test
    void getCourse_shouldThrowResourceNotFoundException_whenCourseNotFound() {

        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourse(99L));
    }

    @Test
    void deleteCourse_shouldDeleteSuccessfully_whenCourseExists() throws ResourceNotFoundException {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCourse_shouldThrowResourceNotFoundException_whenCourseNotFound() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> courseService.deleteCourse(99L));
        verify(courseRepository, never()).deleteById(any());
    }

    @Test
    void updateCourse_shouldUpdateFields_whenCourseExists() throws ResourceNotFoundException {
        CourseRequestDto updateDto = new CourseRequestDto("Algoritmos Avanzados", null, null, 5);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);

        CourseResponseDto result = courseService.updateCourse(1L, updateDto);

        assertNotNull(result);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourse_shouldThrowResourceNotFoundException_whenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.updateCourse(99L, sampleRequestDto));
    }

    @Test
    void getAllCourses_shouldReturnListOfCourses() {
        List<Course> courses = List.of(sampleCourse,
                new Course("Física", 3, "Física básica", 2));
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseResponseDto> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    void getCoursesByTeacher_shouldReturnCourses_whenTeacherExists() throws ResourceNotFoundException {
        Teacher teacher = new Teacher("Ana", "López", 50.0, "ana@uni.edu", null);
        teacher.setCourses(List.of(sampleCourse));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        List<CourseResponseDto> result = courseService.getCoursesByTeacher(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getCoursesByTeacher_shouldThrowResourceNotFoundException_whenTeacherNotFound() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCoursesByTeacher(99L));
    }

    @Test
    void addStudentToCourse_shouldReturnDto_whenBothExist() throws ResourceNotFoundException {
        Student student = new Student("Carlos", "Ríos", "carlos@uni.edu", null, new ArrayList<>());
        student.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);

        var result = courseService.addStudentToCourse(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getCourseId());
        assertEquals(1L, result.getStudentId());
        assertEquals("Algoritmos", result.getCourseName());
        assertEquals("Carlos", result.getStudentName());
    }

    @Test
    void addStudentToCourse_shouldThrowResourceNotFoundException_whenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.addStudentToCourse(99L, 1L));
    }

    @Test
    void addStudentToCourse_shouldThrowResourceNotFoundException_whenStudentNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.addStudentToCourse(1L, 99L));
    }
}
