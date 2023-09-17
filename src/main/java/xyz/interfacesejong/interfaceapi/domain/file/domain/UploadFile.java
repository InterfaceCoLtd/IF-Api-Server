package xyz.interfacesejong.interfaceapi.domain.file.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 파일 id
    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "board_id")
    private Board board;  // 게시글 id
    @Column
    private String originalName;  // 파일 원래이름
    @Column
    private String saveName;  // 파일 업로드 이름
    @Column
    private long size;  // 파일 크기
    @Column
    private String savePath;  // 파일 저장 위치

    @Builder
    public UploadFile(String originalName, String saveName, long size, String savePath) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.savePath = savePath;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
