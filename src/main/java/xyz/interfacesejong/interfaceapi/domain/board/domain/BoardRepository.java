package xyz.interfacesejong.interfaceapi.domain.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Id> {
    Optional<Board> findById(Long id);
}
