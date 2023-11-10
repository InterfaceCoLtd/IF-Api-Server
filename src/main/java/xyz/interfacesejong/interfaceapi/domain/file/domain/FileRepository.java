package xyz.interfacesejong.interfaceapi.domain.file.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
    Optional<List<UploadFile>> findByBoardId(Long id);

    @Query("SELECT f.saveName FROM UploadFile f WHERE f.board.id = :boardId")
    List<String> findSaveNames(Long boardId);

    Boolean existsByBoard_Id(Long boardId);
}
