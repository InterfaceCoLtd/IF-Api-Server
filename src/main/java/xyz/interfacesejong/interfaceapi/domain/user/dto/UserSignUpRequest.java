package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class UserSignUpRequest {
    private String email;

    private String password;

    public UserSignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
