package xyz.interfacesejong.interfaceapi.domain.board.dto;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;

import javax.persistence.Access;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardRequest {
    private String title;
    private String content;
    private Long userId;

    @Builder
    public BoardRequest(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = board.getWriter().getId();
    }

    public BoardRequest(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
