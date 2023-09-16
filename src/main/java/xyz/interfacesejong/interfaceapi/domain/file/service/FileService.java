package xyz.interfacesejong.interfaceapi.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.board.domain.BoardRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.FileRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.global.util.FileUtils;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileUtils fileUtils;
    public void saveFiles(Board board, List<UploadFile> fileList) {
        if(CollectionUtils.isEmpty(fileList)) return;

        for(UploadFile file : fileList) {
            file.setBoard(board);
            fileRepository.save(file);
        }
    }

    public void deleteFilesByBoardId(Long id) {
        // 게시글 삭제 시 첨부파일 삭제
        List<UploadFile> uploadFileList = fileRepository.findByBoardId(id);

        if(uploadFileList.isEmpty()) return;

        for(UploadFile uploadFile : uploadFileList) {
            fileUtils.deleteFile(uploadFile);
            fileRepository.delete(uploadFile);
        }
    }
}
