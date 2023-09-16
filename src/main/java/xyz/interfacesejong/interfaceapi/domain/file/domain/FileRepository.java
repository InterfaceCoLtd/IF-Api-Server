package xyz.interfacesejong.interfaceapi.domain.file.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
    List<UploadFile> findByBoardId(Long id);
}
