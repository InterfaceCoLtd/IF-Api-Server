package xyz.interfacesejong.interfaceapi.domain.user.domain;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.user.dto.SejongStudentAuthResponse;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String email; //val

    private String password; //var

    private Integer generation; //var

    @Column(length = 11)
    private String phoneNumber; //var

    @Column(length = 32)
    private String githubId; //var

    @Column(length = 32)
    private String discordId; //var

    @Column(length = 16)
    private String username; //인증정보 val

    private Integer studentId; //인증정보 val

    @Column(length = 64)
    private String major; //인증정보 val
    
    private Integer grade; //인증정보 val

    private Boolean enrolled; //인증정보 val

    @Column(columnDefinition = "VARCHAR(255)")
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private AuthLevelType authLevel;

    @Column(columnDefinition = "BINARY(16)")
    private UUID deviceId;

    @Builder
    public User(Long id, String email, String password, AuthLevelType authLevel){
        this.id =id;
        this.email = email;
        this.password = password;
        this.authLevel = authLevel;
    }

    public void updateSejongAuthInfo(SejongStudentAuthResponse sejongStudentAuthResponse){
        this.username = sejongStudentAuthResponse.getStudentName();
        this.studentId = Integer.parseInt(sejongStudentAuthResponse.getStudentId());
        this.major = sejongStudentAuthResponse.getMajor();
        this.grade = Integer.parseInt(sejongStudentAuthResponse.getGrade());
        this.enrolled = Objects.equals(sejongStudentAuthResponse.getEnrolled(), "재학");
    }

    public void changeGeneration(Integer generation) {
        this.generation = generation;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeGithubId(String githubId) {
        this.githubId = githubId;
    }

    public void changeDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public void changeAuthLevel(AuthLevelType authLevel){
        this.authLevel = authLevel;
    }

    public void reRegisterPassword(String password){
        this.password = password;
    }

    public void changeDeviceId(UUID deviceId){
        this.deviceId = deviceId;
    }

    public void changeFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

}
