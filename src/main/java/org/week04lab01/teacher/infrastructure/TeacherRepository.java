package org.week04lab01.teacher.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week04lab01.teacher.domain.Teacher;

import java.time.LocalDate;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByFirstName(String firstName);

    Optional<Teacher> findByLastName(String lastName);

    Optional<Teacher> findByBirthday(LocalDate birthday);
}
