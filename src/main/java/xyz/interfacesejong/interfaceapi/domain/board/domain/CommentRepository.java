package xyz.interfacesejong.interfaceapi.domain.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByBoardId(Long BoardId);
}
