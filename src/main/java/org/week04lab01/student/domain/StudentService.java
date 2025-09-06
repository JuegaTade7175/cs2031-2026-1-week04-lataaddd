package org.week04lab01.student.domain;

import org.springframework.stereotype.Service;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.student.infrastructure.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Long id, Student student) throws ResourceNotFoundException {
        Student existingStudent = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id " + id)
        );

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setBirthday(student.getBirthday());
        existingStudent.setCourses(student.getCourses());
        return studentRepository.save(existingStudent);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByCourse(Long courseId) {
        return studentRepository.findByCoursesId(courseId);
    }
}
