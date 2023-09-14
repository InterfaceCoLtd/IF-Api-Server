package xyz.interfacesejong.interfaceapi.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.board.domain.BoardRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.FileRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;

    public void saveFiles(Board board, List<UploadFile> fileList) {
        if(CollectionUtils.isEmpty(fileList)) return;
        System.out.println("saveFiles board : "+ board.getId());
        for(UploadFile file : fileList) {
            file.setBoard(board);
            fileRepository.save(file);
        }
    }
}
