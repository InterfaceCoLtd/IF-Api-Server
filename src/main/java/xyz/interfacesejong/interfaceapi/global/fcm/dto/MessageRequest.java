package xyz.interfacesejong.interfaceapi.global.fcm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Data
public class MessageRequest {
    private String title;
    private String body;
}
