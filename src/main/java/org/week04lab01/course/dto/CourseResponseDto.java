package org.week04lab01.course.dto;

public class CourseResponseDto {
    Long id;
    String name;
    Integer creditNumber;
    String description;
    Integer weeklyHours;

    public CourseResponseDto() {}

    public CourseResponseDto(Long id, String name, Integer creditNumber, String description, Integer weeklyHours) {
        this.id = id;
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
}
