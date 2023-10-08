package xyz.interfacesejong.interfaceapi.domain.board.dto;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;

import javax.persistence.Access;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {
    private String title;
    private String content;
    private Long userId;
    private Long scheduleId;
    private Long subjectId;

    public BoardRequest(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = board.getWriter().getId();
        this.scheduleId = board.getScheduleId();
        this.subjectId = board.getSubjectId();
    }

    @Builder
    public BoardRequest(String title, String content, Long userId, Long scheduleId, Long subjectId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.subjectId = subjectId;
    }
}
