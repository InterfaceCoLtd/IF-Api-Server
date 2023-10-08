package xyz.interfacesejong.interfaceapi.domain.board.domain;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(String title, String content, User writer, List<UploadFile> uploadFiles,List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.uploadFiles = uploadFiles;
        this.comments=comments;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
