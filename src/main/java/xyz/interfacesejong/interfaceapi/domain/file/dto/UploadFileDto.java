package xyz.interfacesejong.interfaceapi.domain.file.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFileDto {
    private Long id;
    private Long boardId;
    private String originalName;
    private String saveName;
    private long size;
    private String savePath;

    @Builder
    public UploadFileDto(UploadFile uploadFile) {
        this.id = uploadFile.getId();
        this.boardId = uploadFile.getBoard().getId();
        this.originalName = uploadFile.getOriginalName();
        this.saveName = uploadFile.getSaveName();
        this.size = uploadFile.getSize();
        this.savePath = uploadFile.getSavePath();
    }

    public UploadFileDto(Long id, Long boardId, String originalName, String saveName, long size, String savePath) {
        this.id = id;
        this.boardId = boardId;
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.savePath = savePath;
    }
}
