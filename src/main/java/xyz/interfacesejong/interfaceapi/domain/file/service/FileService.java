package xyz.interfacesejong.interfaceapi.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.file.domain.FileRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.dto.UploadFileDto;
import xyz.interfacesejong.interfaceapi.global.util.FileUtils;

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

        for(UploadFile file : fileList) {
            file.setBoard(board);
            fileRepository.save(file);
        }
    }

    @Transactional
    public UploadFileDto getUploadFile(Long id) {
        UploadFile uploadFile = fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 첨부파일이 없습니다"));
        UploadFileDto uploadFileDto = UploadFileDto.builder().uploadFile(uploadFile).build();
        return uploadFileDto;
    }

    @Transactional
    public List<UploadFileDto> getAllUploadFiles(Long id) {
        List<UploadFile> uploadFileList = fileRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물에 첨부파일이 존재하지 않습니다."));
        List<UploadFileDto> uploadFileDtoList = new ArrayList<>();

        for(UploadFile uploadFile : uploadFileList) {
            uploadFileDtoList.add(UploadFileDto.builder().uploadFile(uploadFile).build());
        }

        return uploadFileDtoList;
    }

    @Transactional
    public void deleteFilesByBoardId(Long id) {
        // 게시글 삭제 시 첨부파일 삭제
        List<UploadFile> uploadFileList = fileRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물에 첨부파일이 존재하지 않습니다"));

        for(UploadFile uploadFile : uploadFileList) {
            fileUtils.deleteFile(uploadFile);
            fileRepository.delete(uploadFile);
        }
    }
}
