package xyz.interfacesejong.interfaceapi.domain.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findById(Board board);

}

