package xyz.interfacesejong.interfaceapi.domain.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardResponse;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtils {
    @Value("${uploadPath}")
    private String rootUploadPath;

    // 다중 파일 업로드
    public List<UploadFile> uploadFiles(List<MultipartFile> multipartFiles, BoardResponse boardResponse) {
        List<UploadFile> files = new ArrayList<>();
        int idx = 1;
        for(MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty()) continue;
            files.add(uploadFile(multipartFile, boardResponse, idx));
            idx++;
        }
        return files;
    }

    // 단일 파일 업로드
    public UploadFile uploadFile(MultipartFile multipartFile, BoardResponse boardResponse, int idx) {
        if(multipartFile.isEmpty()) return null;

        String saveName = createSaveFileName(multipartFile.getOriginalFilename(), boardResponse, idx);
        String date = boardResponse.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 기본 업로드 위치 + 게시글 PK 디렉터리 생성
        String uploadDir = rootUploadPath + File.separator + boardResponse.getId();
        File dir = new File(uploadDir);
        if(!dir.exists())
            dir.mkdir();

        // 파일 업로드
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path targetPath = path.resolve(saveName).normalize();
        String savePath = targetPath.toString();

        try {
            multipartFile.transferTo(targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return UploadFile.builder()
                .originalName(multipartFile.getOriginalFilename())
                .saveName("https://api.interfacesejong.xyz/image/" + boardResponse.getId() + "/" + saveName)
                .size(multipartFile.getSize())
                .savePath(savePath).build();
    }

    // 디스크에 저장할 파일명
    private String createSaveFileName(String filename, BoardResponse boardResponse, int idx) {
        String newName = new StringBuilder()
                .append(boardResponse.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).append("_")
                .append(boardResponse.getId().toString()).append("_")
                .append(boardResponse.getUserId().toString()).append("_")
                .append(idx)
                .toString();
        String ext = StringUtils.getFilenameExtension(filename);
        return newName + "." + ext;
    }

    public void deleteFile(UploadFile uploadFile) {
        try {
            Path file = Paths.get(uploadFile.getSavePath());
            Files.deleteIfExists(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
