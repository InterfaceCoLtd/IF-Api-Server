package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import xyz.interfacesejong.interfaceapi.domain.user.domain.AuthLevelType;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class UserSignResponse {
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

    private byte[] deviceId;

    public UserSignResponse(User user) {
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
        this.deviceId = user.getDeviceId();
    }
}


