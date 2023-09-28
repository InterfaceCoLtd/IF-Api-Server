package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class UserNewPasswordRequest {
    private String newPassword;
}
