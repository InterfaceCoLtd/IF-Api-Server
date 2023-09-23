package xyz.interfacesejong.interfaceapi.domain.user.domain;

import lombok.*;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    @Builder
    public User(Long id, String email, String password){
        this.id =id;
        this.email = email;
        this.password = password;
    }
}
