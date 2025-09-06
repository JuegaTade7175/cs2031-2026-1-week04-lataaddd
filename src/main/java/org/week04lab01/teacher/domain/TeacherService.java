package org.week04lab01.teacher.domain;

import org.springframework.stereotype.Service;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.teacher.infrastructure.TeacherRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TeacherService {

    private TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public boolean deleteTeacher(Long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Teacher updateTeacher(Long id, Teacher teacher) {
        Teacher teacherToUpdate = teacherRepository.findById(id).orElse(null);
        if (teacherToUpdate == null) {
            return null;
        }
        teacherToUpdate.setFirstName(teacher.getFirstName());
        teacherToUpdate.setLastName(teacher.getLastName());
        teacherToUpdate.setHourlyWage(teacher.getHourlyWage());
        teacherToUpdate.setEmail(teacher.getEmail());
        teacherToUpdate.setBirthday(teacher.getBirthday());
        teacherToUpdate.setCourses(teacher.getCourses());
        return teacherRepository.save(teacherToUpdate);
    }

    public Teacher getTeacherByEmail(String email) throws ResourceNotFoundException {
        return teacherRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Teacher with email " + email + " not found")
                );
    }

    public Teacher getTeacherByFirstName(String firstName) throws ResourceNotFoundException {
        return teacherRepository
                .findByFirstName(firstName)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Teacher with first name " + firstName + " not found")
                );
    }

    public Teacher getTeacherByLastName(String lastName) throws ResourceNotFoundException {
        return teacherRepository
                .findByLastName(lastName)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Teacher with last name " + lastName + " not found")
                );
    }

    public Teacher getTeacherByBirthday(LocalDate birthday) throws ResourceNotFoundException {
        return teacherRepository
                .findByBirthday(birthday)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Teacher with birthday " + birthday + " not found")
                );
    }

}
