package org.week04lab01.course.domain;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.week04lab01.course.dto.AddStudentToCourseDto;
import org.week04lab01.course.dto.CourseRequestDto;
import org.week04lab01.course.dto.CourseResponseDto;
import org.week04lab01.course.infrastructure.CourseRepository;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.student.domain.Student;
import org.week04lab01.student.infrastructure.StudentRepository;
import org.week04lab01.teacher.domain.Teacher;
import org.week04lab01.teacher.infrastructure.TeacherRepository;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;


    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = new ModelMapper();
    }

    public CourseResponseDto createCourse(CourseRequestDto course) {
        Course courseEntity = modelMapper.map(course, Course.class);
        Course savedCourse = courseRepository.save(courseEntity);

        return modelMapper.map(savedCourse, CourseResponseDto.class);
    }

    public CourseResponseDto getCourse(Long id) throws ResourceNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Course not found with id " + id)
                );

        return modelMapper.map(course, CourseResponseDto.class);
    }

    public void deleteCourse(Long id) throws ResourceNotFoundException {
        if (!courseRepository.existsById(id))
            throw new ResourceNotFoundException("Course not found with id " + id);

        courseRepository.deleteById(id);
    }

    public CourseResponseDto updateCourse(Long id, CourseRequestDto courseDto) throws ResourceNotFoundException {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Course not found with id " + id)
                );

        if (courseDto.getDescription() != null)
            existingCourse.setDescription(courseDto.getDescription());
        if (courseDto.getName() != null)
            existingCourse.setName(courseDto.getName());
        if (courseDto.getCreditNumber() != null)
            existingCourse.setCreditNumber(courseDto.getCreditNumber());
        if (courseDto.getWeeklyHours() != null)
            existingCourse.setWeeklyHours(courseDto.getWeeklyHours());

        Course updatedCourse = courseRepository.save(existingCourse);
        return modelMapper.map(updatedCourse, CourseResponseDto.class);
    }

    public List<CourseResponseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> modelMapper.map(course, CourseResponseDto.class))
                .toList();
    }

    public List<CourseResponseDto> getCoursesByTeacher(Long teacherId) throws ResourceNotFoundException {
        Teacher currentTeacher = teacherRepository.findById(teacherId).
                orElseThrow(
                        () -> new ResourceNotFoundException("Teacher not found with id " + teacherId)
                );

        List<Course> courses = currentTeacher.getCourses();
        return courses.stream()
                .map(course -> modelMapper.map(course, CourseResponseDto.class))
                .toList();
    }

    public AddStudentToCourseDto addStudentToCourse(Long courseId, Long studentId) throws ResourceNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Course not found with id " + courseId)
                );

        Student student = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found with id " + studentId)
                );

        if (course != null && student != null) {
            course.getStudents().add(student);
            courseRepository.save(course);
        }

        var responseDto = new AddStudentToCourseDto();
        responseDto.setCourseId(courseId);
        responseDto.setStudentId(studentId);
        responseDto.setCourseName(course.getName());
        responseDto.setStudentName(student.getFirstName());
        return responseDto;
    }
}

