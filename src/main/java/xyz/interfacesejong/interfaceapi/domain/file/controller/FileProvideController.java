package xyz.interfacesejong.interfaceapi.domain.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("api/")
@RequiredArgsConstructor
public class FileProvideController {

    FileService fileService;

    private static final String IMAGES_DIR = "/home/ec2-user/image/20231010";
    @GetMapping("image/{src-url}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable(value = "src-url") String srcUrl) throws FileNotFoundException {
        Path imagePath = Paths.get(IMAGES_DIR + srcUrl);

        if (!Files.exists(imagePath) || Files.isDirectory(imagePath)) {
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
