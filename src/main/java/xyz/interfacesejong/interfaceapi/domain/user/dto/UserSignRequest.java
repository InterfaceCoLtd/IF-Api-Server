package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class UserSignRequest {
    private String email;

    private String password;

    private UUID deviceId;
}
