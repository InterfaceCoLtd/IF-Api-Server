package xyz.interfacesejong.interfaceapi.domain.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.file.dto.UploadFileResponse;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Timer
    @GetMapping("/file/{id}")
    public ResponseEntity<UploadFileResponse> getUploadFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getUploadFile(id));
    }

    @Timer
    @GetMapping("/board/{id}")
    public ResponseEntity<List<UploadFileResponse>> getAllUploadFiles(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(fileService.getAllUploadFiles(id));
    }
}
