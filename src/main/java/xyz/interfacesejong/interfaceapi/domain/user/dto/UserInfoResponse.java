package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class UserInfoResponse {
    private Long id;

    private String email; //val

    private Integer generation; //var

    private String phoneNumber; //var

    private String githubId; //var

    private String discordId; //var

    private String username; //인증정보 val

    private Integer studentId; //인증정보 val

    private String major; //인증정보 val

    private Integer grade; //인증정보 val

    private Boolean enrolled; //인증정보 val

    @Enumerated(EnumType.STRING)
    private AuthLevelType authLevel;

    @Builder

    public UserInfoResponse(Long id, String email, Integer generation, String phoneNumber, String githubId, String discordId, String username, Integer studentId, String major, Integer grade, Boolean enrolled, AuthLevelType authLevel) {
        this.id = id;
        this.email = email;
        this.generation = generation;
        this.phoneNumber = phoneNumber;
        this.githubId = githubId;
        this.discordId = discordId;
        this.username = username;
        this.studentId = studentId;
        this.major = major;
        this.grade = grade;
        this.enrolled = enrolled;
        this.authLevel = authLevel;
    }

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.generation = user.getGeneration();
        this.phoneNumber = user.getPhoneNumber();
        this.githubId = user.getGithubId();
        this.discordId = user.getDiscordId();
        this.username = user.getUsername();
        this.studentId = user.getStudentId();
        this.major = user.getMajor();
        this.grade = user.getGrade();
        this.enrolled = user.getEnrolled();
        this.authLevel = user.getAuthLevel();
    }
}
