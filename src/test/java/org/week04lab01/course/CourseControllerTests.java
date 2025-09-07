package org.week04lab01.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.week04lab01.course.domain.Course;
import org.week04lab01.course.dto.CourseRequestDto;
import org.week04lab01.course.dto.CourseResponseDto;
import org.week04lab01.course.infrastructure.CourseRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") /// Uses application-test.properties
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldReturnOkWhenCreatingCourse() throws Exception {
        /// POST /courses

        /// Arrange
        CourseRequestDto courseDto = new CourseRequestDto(
                "Matemáticas",
                5,
                "Curso de matemáticas básicas",
                3
        );

        String payload = objectMapper.writeValueAsString(courseDto);

        /// Act
        MvcResult res = mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();
        /// Assert
        String responseContent = res.getResponse().getContentAsString();
        CourseResponseDto createdCourse = objectMapper.readValue(responseContent, CourseResponseDto.class);

        Course queriedCourse = courseRepository.findById(createdCourse.getId()).orElse(null);

        assertNotNull(queriedCourse);
        assertEquals("Matemáticas", queriedCourse.getName());
        assertEquals(5, queriedCourse.getCreditNumber());
        assertEquals("Curso de matemáticas básicas", queriedCourse.getDescription());
        assertEquals(3, queriedCourse.getWeeklyHours());
    }

    @Test
    public void shouldReturnOkWhenUpdatingCourse() throws Exception {
        /// PATCH /courses/{id}

        /// Arrange
        Course existingCourse = new Course(
                "Historia",
                4,
                "Curso de historia mundial",
                2);
        Course savedCourse = courseRepository.save(existingCourse);

        CourseRequestDto updatedCourseDto = new CourseRequestDto(
                "Historia Moderna",
                4,
                "Curso de historia mundial",
                2
        );

        String payload = objectMapper.writeValueAsString(updatedCourseDto);

        /// Act
        mockMvc.perform(patch("/courses/" + savedCourse.getId())
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isOk());

        /// Assert
        Course queriedCourse = courseRepository.findById(savedCourse.getId()).orElse(null);

        assertNotNull(queriedCourse);
        assertEquals("Historia Moderna", queriedCourse.getName());
        assertEquals(4, queriedCourse.getCreditNumber());
        assertEquals("Curso de historia mundial", queriedCourse.getDescription());
        assertEquals(2, queriedCourse.getWeeklyHours());
    }

}

