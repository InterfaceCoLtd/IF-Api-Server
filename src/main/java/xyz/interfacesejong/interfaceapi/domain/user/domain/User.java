package xyz.interfacesejong.interfaceapi.domain.user.domain;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.user.domain.sejongAuth.dto.SejongStudentAuthResponse;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String email;

    private String password;

    private Integer generation;

    @Column(length = 11)
    private String phoneNumber;

    @Column(length = 32)
    private String githubId;

    @Column(length = 32)
    private String discordId;

    @Column(length = 16)
    private String username; //인증정보

    private Integer studentId; //인증정보

    @Column(length = 64)
    private String major; //인증정보
    
    private Integer grade; //인증정보

    private Boolean enrolled; //인증정보

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAuth auth;

    @Builder
    public User(Long id, String email, String password){
        this.id =id;
        this.email = email;
        this.password = password;
    }

    public void updateSejongAuthInfo(SejongStudentAuthResponse sejongStudentAuthResponse){
        this.username = sejongStudentAuthResponse.getStudentName();
        this.studentId = Integer.parseInt(sejongStudentAuthResponse.getStudentId());
        this.major = sejongStudentAuthResponse.getMajor();
        this.grade = Integer.parseInt(sejongStudentAuthResponse.getGrade());
        this.enrolled = Objects.equals(sejongStudentAuthResponse.getEnrolled(), "재학중");
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
}
