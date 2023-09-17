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

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, User writer, List<UploadFile> uploadFiles) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.uploadFiles = uploadFiles;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
