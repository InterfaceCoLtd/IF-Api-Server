package xyz.interfacesejong.interfaceapi.domain.board.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String content; //댓글 내용

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer; // 사용자 id

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board; //게시글 id

    @Builder
    public Comment(Long id,String content,User writer,Board board){
        this.id=id;
        this.content=content;
        this.writer=writer;
        this.board=board;
    }
}
