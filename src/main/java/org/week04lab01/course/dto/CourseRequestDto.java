package org.week04lab01.course.dto;


public class CourseRequestDto {
    private String name;
    private Integer creditNumber;
    private String description;
    private Integer weeklyHours;

    public CourseRequestDto(){}

    public CourseRequestDto(String name, Integer creditNumber, String description, Integer weeklyHours) {
        this.name = name;
        this.creditNumber = creditNumber;
        this.description = description;
        this.weeklyHours = weeklyHours;
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
