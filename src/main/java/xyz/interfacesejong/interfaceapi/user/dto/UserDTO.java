package xyz.interfacesejong.interfaceapi.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private Long id;
    private String email;
    private String password;

    @Builder
    public UserDTO(Long id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
