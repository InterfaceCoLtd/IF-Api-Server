package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class SejongStudentAuthResponse {
    private String major;

    private String studentId;

    private String studentName;

    private String grade;

    private String enrolled;

    @Builder
    SejongStudentAuthResponse(String major, String studentId, String studentName, String grade, String enrolled){
        this.major = major;
        this.studentId = studentId;
        this.studentName = studentName;
        this.grade = grade;
        this.enrolled = enrolled;
    }
}
