package org.week04lab01.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.week04lab01.AbstractContainerBaseTest;
import org.week04lab01.course.domain.Course;
import org.week04lab01.course.infrastructure.CourseRepository;

@DataJpaTest
public class CourseRepositoryTests extends AbstractContainerBaseTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testCreateAndSaveCourse() {
        // Arrange
        Course course = new Course(
                "Matemáticas",
                5,
                "Curso de matemáticas básicas",
                3);

        // Act
        Course savedCourse = courseRepository.save(course);

        // Assert
        assert savedCourse.getId() != null;
        assert savedCourse.getName().equals("Matemáticas");
        assert savedCourse.getCreditNumber().equals(5);
        assert savedCourse.getDescription().equals("Curso de matemáticas básicas");
        assert savedCourse.getWeeklyHours().equals(3);
    }

    @Test
    void testUpdateCourse() {
        // Arrange
        Course course = new Course(
                "Historia",
                4,
                "Curso de historia mundial",
                2);
        Course savedCourse = courseRepository.save(course);

        // Act
        savedCourse.setName("Historia Moderna");
        courseRepository.save(savedCourse);
        Course updatedCourse = courseRepository.findById(savedCourse.getId()).orElse(null);

        // Assert
        assert updatedCourse != null;
        assert updatedCourse.getName().equals("Historia Moderna");
        assert updatedCourse.getCreditNumber().equals(4);
        assert updatedCourse.getDescription().equals("Curso de historia mundial");
        assert updatedCourse.getWeeklyHours().equals(2);
    }
}
