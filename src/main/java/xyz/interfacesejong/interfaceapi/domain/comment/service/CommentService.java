package xyz.interfacesejong.interfaceapi.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.board.domain.BoardRepository;
import xyz.interfacesejong.interfaceapi.domain.comment.domain.Comment;
import xyz.interfacesejong.interfaceapi.domain.comment.domain.CommentRepository;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //게시글 id로 댓글 리스트 불러오기
    @Transactional
    public List<Comment> findCommentsByBoardId(Long boardId) throws EntityNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다."));

        return commentRepository.findById(board);
    }

    //댓글 저장
    @Transactional
    public void saveComment(Long boardId, Long userId, String content) throws Exception {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다."));

        Comment comment = Comment.builder()
                .content(content)
                .writer(userRepository.findById(userId)
                        .orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다.")))
                .board(board)
                .build();

        commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long id){
        Comment comment= commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 없습니다."));

        commentRepository.delete(comment);
    }

}
