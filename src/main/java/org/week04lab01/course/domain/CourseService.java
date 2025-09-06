package org.week04lab01.course.domain;

import org.springframework.stereotype.Service;
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


    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id).orElse(null);
        if (existingCourse == null) {
            return null;
        }
        existingCourse.setName(course.getName());
        existingCourse.setStudents(course.getStudents());
        return courseRepository.save(existingCourse);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByStudent(Long studentId) {
        return courseRepository.findByStudentsId(studentId);
    }

    public void addStudentToCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (course != null && student != null) {
            course.getStudents().add(student);
            courseRepository.save(course);
        }
    }

    public List<Course> getCoursesByTeacher(Long teacherId) throws ResourceNotFoundException {
        Teacher currentTeacher = teacherRepository.findById(teacherId).
                orElseThrow(
                        () -> new ResourceNotFoundException("Teacher not found with id " + teacherId)
                );

        return currentTeacher.getCourses();
    }
}

