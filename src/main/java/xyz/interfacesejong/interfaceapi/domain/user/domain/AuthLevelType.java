package xyz.interfacesejong.interfaceapi.domain.user.domain;

import lombok.Getter;

@Getter
public enum AuthLevelType {
    NEW_ACCOUNT("신규 계정"),
    MAIL_VERIFIED("메일 인증"),
    STUDENT_VERIFIED("학생 인증"),
    MEMBER_VERIFIED("부원 인증"),
    ADMIN("관리자"),
    SUPER_ADMIN("메인 관리자");

    private final String description;

    AuthLevelType(String description) {
        this.description = description;
    }


}
