package xyz.interfacesejong.interfaceapi.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.interfacesejong.interfaceapi.domain.file.domain.FileRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public void saveFiles(Long boardId, List<UploadFile> fileList) {
        if(CollectionUtils.isEmpty(fileList)) return;
        for(UploadFile file : fileList) {
            file.setBoardId(boardId);
            fileRepository.save(file);
        }
    }
}
