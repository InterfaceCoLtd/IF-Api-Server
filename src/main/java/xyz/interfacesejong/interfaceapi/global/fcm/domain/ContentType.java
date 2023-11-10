package xyz.interfacesejong.interfaceapi.global.fcm.domain;

import lombok.Getter;

@Getter
public enum ContentType {
    NOTICE("공지사항 등록"),
    SCHEDULE("일정 등록"),
    VOTE("투표 등록"),
    CUSTOM("커스텀 인증");

    private final String description;

    ContentType(String description) {
        this.description = description;
    }
}
