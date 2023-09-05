package xyz.interfacesejong.interfaceapi.sejongAuth.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StudentDTO {
    private String major;

    private String studentId;

    private String studentName;

    private String grade;

    private String enrolled;

    @Builder
    StudentDTO(String major, String studentId, String studentName, String grade, String enrolled){
        this.major = major;
        this.studentId = studentId;
        this.studentName = studentName;
        this.grade = grade;
        this.enrolled = enrolled;
    }
}
