package xyz.interfacesejong.interfaceapi.domain.board.domain;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @Builder
    public Board(String title, String content, User writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
