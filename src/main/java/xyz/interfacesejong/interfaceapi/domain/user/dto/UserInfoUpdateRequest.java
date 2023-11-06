package xyz.interfacesejong.interfaceapi.domain.user.dto;

import lombok.*;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class UserInfoUpdateRequest {
    private Integer generation;
    private String phoneNumber;
    private String githubId;
    private String discordId;
    private String fcmToken;
    private UUID deviceId;
}
