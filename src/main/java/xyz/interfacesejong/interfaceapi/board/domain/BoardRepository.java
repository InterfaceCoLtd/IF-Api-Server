package xyz.interfacesejong.interfaceapi.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface BoardRepository extends JpaRepository<Board, Id> {
    Board findById(Long id);
}
