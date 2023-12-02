package xyz.interfacesejong.interfaceapi.domain.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileProvideController {

    @Value("${uploadPath}")
    private String IMAGES_DIR = "C:/CODE/IF/image/";

    @GetMapping("image")
    @Operation(summary = "년도별 홍보 이미지 목록",
            description = "year에 해당하는 년도에 대한 모든 홍보 이미지 url를 제공한다.\n\n" +
                    "response에 들어 있는 주소로 Get요청시 이미지를 확인할 수 있다.")
    public ResponseEntity<List<String>> findIntroductionPhotoNames(@RequestParam String year){
        File dir = new File(IMAGES_DIR + year);

        if (dir.exists() && dir.isDirectory()) {
            if (dir.listFiles() == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<String> imageFileNames = Arrays.stream(dir.listFiles())
                    .filter(file ->
                            file.getName().toLowerCase().endsWith(".jpg")
                            || file.getName().toLowerCase().endsWith(".png"))
                    .map(file -> "https://api.interfacesejong.xyz/api/image/" + year + "/" + file.getName())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(imageFileNames, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("image/{boardId}/{src-url}")
    @Operation(summary = "image 제공",
            description = "게시글 목록인 boardId와 게시글에 속한 이미지명에 해당하는 이미지를 반환한다.")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String boardId,
                                                        @PathVariable(value = "src-url") String srcUrl) throws FileNotFoundException {
        Path imagePath = Paths.get(IMAGES_DIR + "/" + boardId + "/" + srcUrl);

        if (!Files.exists(imagePath) || Files.isDirectory(imagePath)) {
            log.info("[getImage] invalid image path {}", imagePath);
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType;

        if (srcUrl.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (srcUrl.endsWith(".jpg") || srcUrl.endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (srcUrl.endsWith(".gif")){
            mediaType = MediaType.IMAGE_GIF;
        } else if (srcUrl.endsWith(".svg")) {
            mediaType = MediaType.valueOf("image/svg+xml");
        } else {
            throw new FileNotFoundException();
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(imagePath.toFile()));

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

}
