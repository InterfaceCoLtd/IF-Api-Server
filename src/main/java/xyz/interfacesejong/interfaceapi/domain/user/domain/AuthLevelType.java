package xyz.interfacesejong.interfaceapi.domain.user.domain;

import lombok.Getter;

@Getter
public enum AuthLevelType {
    DELETE_ACCOUNT("삭제된 계정", -1),
    NEW_ACCOUNT("신규 계정", 0),
    MAIL_VERIFIED("메일 인증", 1),
    STUDENT_VERIFIED("학생 인증", 2),
    MEMBER_VERIFIED("부원 인증", 3),
    ADMIN("관리자", 4),
    SUPER_ADMIN("메인 관리자", 5);

    private final String description;
    private final Integer level;

    AuthLevelType(String description, Integer level) {
        this.description = description;
        this.level = level;
    }


}
