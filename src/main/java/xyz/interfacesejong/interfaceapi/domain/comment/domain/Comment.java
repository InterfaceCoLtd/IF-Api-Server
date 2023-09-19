package xyz.interfacesejong.interfaceapi.domain.comment.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //댓글 id

    @Column
    private String c_content; //댓글 내용
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User c_writer; // 사용자 id

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board; //게시글 id

    @Builder
    public Comment(String c_content, User c_writer, Board board) {
        this.c_content = c_content;
        this.c_writer = c_writer;
        this.board = board;
    }
}
