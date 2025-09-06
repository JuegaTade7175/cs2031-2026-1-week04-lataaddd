package org.week04lab01.course.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week04lab01.course.domain.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStudentsId(Long studentId);
}
