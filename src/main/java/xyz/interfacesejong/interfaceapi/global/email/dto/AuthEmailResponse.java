package xyz.interfacesejong.interfaceapi.global.email.dto;

import lombok.Getter;

@Getter
public class AuthEmailResponse {
    String authCode;

    String toMail;

    public AuthEmailResponse(String authCode, String toMail) {
        this.authCode = authCode;
        this.toMail = toMail;
    }
}
