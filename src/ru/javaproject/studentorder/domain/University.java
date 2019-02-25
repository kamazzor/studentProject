package ru.javaproject.studentorder.domain;

public class University {
    private Long universityId;
    private String universityName;

    public University() {
    }

    public University(Long university_id, String university_name) {
        this.universityId = university_id;
        this.universityName = university_name;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
}
