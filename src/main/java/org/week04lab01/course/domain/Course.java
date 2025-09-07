package org.week04lab01.course.domain;

import jakarta.persistence.*;
import org.week04lab01.student.domain.Student;
import org.week04lab01.teacher.domain.Teacher;

import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer creditNumber;

    private String description;

    private Integer weeklyHours;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    public Course(){}

    public Course(String name, Integer creditNumber, String description, Integer weeklyHours) {
        this.name = name;
        this.creditNumber = creditNumber;
        this.description = description;
        this.weeklyHours = weeklyHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(Integer creditNumber) {
        this.creditNumber = creditNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(Integer weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
