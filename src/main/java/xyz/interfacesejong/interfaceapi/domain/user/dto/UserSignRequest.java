package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignRequest {
    private String email;

    private String password;

    public UserSignRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
