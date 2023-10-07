package xyz.interfacesejong.interfaceapi.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.file.domain.FileRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.dto.UploadFileDto;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileUtils fileUtils;
    @Transactional
    public void saveFiles(Board board, List<UploadFile> fileList) {
        if(CollectionUtils.isEmpty(fileList)) return;
        fileList.stream().forEach(uploadFile -> {
            uploadFile.setBoard(board);
            fileRepository.save(uploadFile);
        });
    }

    @Transactional
    public UploadFileDto getUploadFile(Long id) {
        UploadFile uploadFile = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 첨부파일이 없습니다"));
        UploadFileDto uploadFileDto = new UploadFileDto(uploadFile);
        return uploadFileDto;
    }

    @Transactional
    public List<UploadFileDto> getAllUploadFiles(Long id) {
        List<UploadFile> uploadFileList = fileRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물에 첨부파일이 존재하지 않습니다."));
        List<UploadFileDto> uploadFileDtoList = new ArrayList<>();
        uploadFileList.stream().forEach(uploadFile -> uploadFileDtoList.add(new UploadFileDto(uploadFile)));
        return uploadFileDtoList;
    }

    @Transactional
    public void deleteFilesByBoardId(Long id) {
        // 게시글 삭제 시 첨부파일 삭제
        List<UploadFile> uploadFileList = fileRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물에 첨부파일이 존재하지 않습니다"));
        uploadFileList.stream().forEach(uploadFile -> {
            fileUtils.deleteFile(uploadFile);
            fileRepository.delete(uploadFile);
        });
    }
}
