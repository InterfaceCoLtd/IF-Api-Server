package xyz.interfacesejong.interfaceapi.domain.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.dto.UploadFileDto;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<UploadFileDto> getUploadFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getUploadFile(id));
    }
    @GetMapping("/board/{id}")
    public ResponseEntity<List<UploadFileDto>> getAllUploadFiles(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(fileService.getAllUploadFiles(id));
    }
}
