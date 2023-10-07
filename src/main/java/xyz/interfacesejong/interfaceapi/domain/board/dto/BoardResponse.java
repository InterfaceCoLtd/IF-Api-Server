package xyz.interfacesejong.interfaceapi.domain.board.dto;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;

    @Builder
    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = board.getWriter().getId();
    }

    public BoardResponse(Long id, String title, String content, Long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
