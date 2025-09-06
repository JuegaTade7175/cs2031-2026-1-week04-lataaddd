package org.week04lab01.teacher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week04lab01.exceptions.ResourceNotFoundException;
import org.week04lab01.teacher.domain.Teacher;
import org.week04lab01.teacher.domain.TeacherService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher createdTeacher = teacherService.createTeacher(teacher);
        return ResponseEntity.ok(createdTeacher);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        boolean deleted = teacherService.deleteTeacher(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
        return updatedTeacher != null ? ResponseEntity.ok(updatedTeacher) : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Teacher> getTeacherByEmail(@PathVariable String email) throws ResourceNotFoundException {
        Teacher teacher = teacherService.getTeacherByEmail(email);
        return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
    }

    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<Teacher> getTeacherByFirstName(@PathVariable String firstName) throws ResourceNotFoundException {
        Teacher teacher = teacherService.getTeacherByFirstName(firstName);
        return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<Teacher> getTeacherByLastName(@PathVariable String lastName) throws ResourceNotFoundException {
        Teacher teacher = teacherService.getTeacherByLastName(lastName);
        return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
    }

    @GetMapping("/birthday/{birthday}")
    public ResponseEntity<Teacher> getTeacherByBirthday(@PathVariable LocalDate birthday) throws ResourceNotFoundException {
        Teacher teacher = teacherService.getTeacherByBirthday(birthday);
        return teacher != null ? ResponseEntity.ok(teacher) : ResponseEntity.notFound().build();
    }
}
