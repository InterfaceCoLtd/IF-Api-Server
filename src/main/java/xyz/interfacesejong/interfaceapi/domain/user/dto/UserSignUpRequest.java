package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class UserSignUpRequest {
    private String email;

    private String password;
}
