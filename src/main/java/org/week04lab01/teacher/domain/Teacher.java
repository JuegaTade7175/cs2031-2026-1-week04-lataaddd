package org.week04lab01.teacher.domain;

import jakarta.persistence.*;
import org.week04lab01.course.domain.Course;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private Double hourlyWage;

    @Column(unique = true)
    private String email;

    private LocalDate birthday;

    @OneToMany
    private List<Course> courses;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, Double hourlyWage, String email, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hourlyWage = hourlyWage;
        this.email = email;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(Double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
