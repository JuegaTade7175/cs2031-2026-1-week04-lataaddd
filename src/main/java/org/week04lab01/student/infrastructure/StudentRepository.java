package org.week04lab01.student.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week04lab01.student.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByCoursesId(Long courseId);
}
