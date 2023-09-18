package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoResponse {
    private Long id;

    private String email;

    @Builder
    UserInfoResponse(Long id, String email, String password){
        this.id = id;
        this.email = email;
    }
}
