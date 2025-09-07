package org.week04lab01.course.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week04lab01.course.domain.CourseService;
import org.week04lab01.course.dto.AddStudentToCourseDto;
import org.week04lab01.course.dto.CourseRequestDto;
import org.week04lab01.course.dto.CourseResponseDto;
import org.week04lab01.exceptions.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto course) {
        return ResponseEntity.ok(
                courseService.createCourse(course)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                courseService.getCourse(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) throws ResourceNotFoundException {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseRequestDto course
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                courseService.updateCourse(id, course)
        );
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        return ResponseEntity.ok(
                courseService.getAllCourses()
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByTeacher(@PathVariable Long teacherId) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                courseService.getCoursesByTeacher(teacherId)
        );
    }

    @PostMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<AddStudentToCourseDto> addStudentToCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(
                courseService.addStudentToCourse(courseId, studentId)
        );
    }
}
