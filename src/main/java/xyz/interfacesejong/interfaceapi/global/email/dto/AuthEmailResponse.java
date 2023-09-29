package xyz.interfacesejong.interfaceapi.global.email.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthEmailResponse {
    private String result;

    private String toMail;

    public AuthEmailResponse(String result, String toMail) {
        this.result = result;
        this.toMail = toMail;
    }
}
