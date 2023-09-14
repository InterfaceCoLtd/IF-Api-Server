package xyz.interfacesejong.interfaceapi.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {
    @Value("${uploadPath}")
    String rootUploadPath;

    // 다중 파일 업로드
    public List<UploadFile> uploadFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> files = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty()) continue;
            files.add(uploadFile(multipartFile));
        }
        return files;
    }

    // 단일 파일 업로드
    public UploadFile uploadFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) return null;

        String saveName = createSaveFileName(multipartFile.getOriginalFilename());
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 기본 업로드 위치 + 날짜 디렉터리 생성
        String uploadDir = rootUploadPath+File.separator+date;
        File dir = new File(uploadDir);
        if(!dir.exists()) dir.mkdir();

        // 파일 업로드
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
        String filename = multipartFile.getOriginalFilename();
        Path targetPath = path.resolve(filename).normalize();

        try {
            multipartFile.transferTo(targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return UploadFile.builder()
                .originalName(multipartFile.getOriginalFilename())
                .saveName(saveName)
                .size(multipartFile.getSize()).build();
    }

    // 디스크에 저장할 파일명
    private String createSaveFileName(String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String ext = StringUtils.getFilenameExtension(filename);
        return uuid+"."+ext;
    }
}
