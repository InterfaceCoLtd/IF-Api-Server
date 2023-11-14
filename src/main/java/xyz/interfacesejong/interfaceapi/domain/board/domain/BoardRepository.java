package xyz.interfacesejong.interfaceapi.domain.board.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.interfacesejong.interfaceapi.domain.board.dto.TitleDto;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Id> {
    Optional<Board> findById(Long id);
    Optional<Board> deleteById(Long id);
    Optional<List<Board>> findByWriterId(Long id);
    Optional<Board> findByScheduleId(Long id);
    Optional<Board> findBySubjectId(Long id);
    @Query("SELECT new xyz.interfacesejong.interfaceapi.domain.board.dto.TitleDto(B.title) FROM Board as B")
    List<TitleDto> getAllTitles();

    Slice<Board> findByOrderByIdDesc(Pageable pageable);
}
