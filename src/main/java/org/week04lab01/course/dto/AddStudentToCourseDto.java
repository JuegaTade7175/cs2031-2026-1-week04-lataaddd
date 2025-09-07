package org.week04lab01.course.dto;

public class AddStudentToCourseDto {
    Long studentId;
    Long courseId;
    String studentName;
    String courseName;

    public AddStudentToCourseDto() {}

    public AddStudentToCourseDto(Long studentId, Long courseId, String studentName, String courseName) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}