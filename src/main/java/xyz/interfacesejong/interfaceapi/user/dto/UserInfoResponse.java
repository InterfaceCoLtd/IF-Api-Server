package xyz.interfacesejong.interfaceapi.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoResponse {
    private Long id;

    private String email;

    private String password;

    @Builder
    UserInfoResponse(Long id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
