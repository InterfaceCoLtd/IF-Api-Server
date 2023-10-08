package xyz.interfacesejong.interfaceapi.domain.file.dto;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFileRequest {
    private Long boardId;
    private String originalName;
    private String saveName;
    private long size;
    private String savePath;

    public UploadFileRequest(UploadFile uploadFile) {
        this.boardId = uploadFile.getBoard().getId();
        this.originalName = uploadFile.getOriginalName();
        this.saveName = uploadFile.getSaveName();
        this.size = uploadFile.getSize();
        this.savePath = uploadFile.getSavePath();
    }

    @Builder
    public UploadFileRequest(Long boardId, String originalName, String saveName, long size, String savePath) {
        this.boardId = boardId;
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.savePath = savePath;
    }
}
