package xyz.interfacesejong.interfaceapi.domain.board.dto;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long scheduleId;
    private Long subjectId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<String> fileNames = new ArrayList<>();

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = board.getWriter().getId();
        this.scheduleId = board.getScheduleId();
        this.subjectId = board.getSubjectId();
        this.createDate = board.getCreatedDate();
        this.updateDate = board.getModifiedDate();
    }

    @Builder
    public BoardResponse(Long id, String title, String content, Long userId, Long scheduleId, Long subjectId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.subjectId = subjectId;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }


}
