package xyz.interfacesejong.interfaceapi.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.board.domain.BoardRepository;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Comment;
import xyz.interfacesejong.interfaceapi.domain.board.domain.CommentRepository;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardDto;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.global.util.FileUtils;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);
    //게시물 저장
    @Transactional
    public void save(BoardDto boardDto) throws Exception {

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(userRepository.findById(boardDto.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다."))).build();

        boardRepository.save(board);
    }

    @Transactional
    public void saveFiles(BoardDto boardDto, List<MultipartFile> multipartFileList) throws Exception {
        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(userRepository.findById(boardDto.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다."))).build();

        boardRepository.save(board);

        List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList);
        fileService.saveFiles(board, uploadFileList);
    }

    // 전체 게시물 불러오기
    @Transactional
    public List<BoardDto> getAllBoards() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for(Board board : boardList) {
            boardDtoList.add(new BoardDto(board));
        }

        return boardDtoList;
        //LOGGER.info("[findAllBoards] : 모든 게시글 조회");
    }

    // 작성자 id로 게시물 불러오기
    @Transactional
    public List<BoardDto> findByUserId(Long id) {
        List<BoardDto> boardDtoList = new ArrayList<>();
        List<Board> boardList = boardRepository.findAll();

        for(Board board : boardList) {
            if(board.getWriter().getId().equals(id)) {
                boardDtoList.add(new BoardDto(board));
            }
        }

        return boardDtoList;
    }

    // id로 게시물 불러오기
    @Transactional
    public BoardDto findById(Long id) throws EntityNotFoundException {
        BoardDto boardDto = BoardDto.builder()
                .board(boardRepository.findById(id).get()).build();

        return boardDto;
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long id) throws EntityNotFoundException {
        Board board=boardRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[delete] 해당 게시물을 찾을 수 없음");
                    return new EntityNotFoundException("해당 게시물이 없습니다.");
                });
        boardRepository.delete(board);
    }

    // 게시물 수정
    @Transactional
    public BoardDto update(Long id, BoardDto updatedBoardDto) throws EntityNotFoundException {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));
        board.update(updatedBoardDto.getTitle(), updatedBoardDto.getContent());

        return new BoardDto(board);
    }

    @Transactional
    public BoardDto updateFiles(Long id, BoardDto updatedBoardDto, List<MultipartFile> multipartFileList) throws EntityNotFoundException {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));
        board.update(updatedBoardDto.getTitle(), updatedBoardDto.getContent());

        fileService.deleteFilesByBoardId(id);
        List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList);
        fileService.saveFiles(board, uploadFileList);

        return new BoardDto(board);
    }

    //게시글 id로 댓글 리스트 불러오기
    @Transactional
    public Optional<List<Comment>> getCommentsByBoardId(Long boardId) throws EntityNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->{
                    LOGGER.info("[getCommentsByBoardId] 해당 게시물을 찾을 수 없음");
                    return new EntityNotFoundException("해당 게시물이 없습니다.");
                });

        LOGGER.info("[getCommentsByBoardId] 댓글 리스트 조회,게시글 ID: {}", boardId);
        return commentRepository.findByBoardId(boardId);
    }

    //댓글 저장
    @Transactional
    public void saveComment(Long boardId, Long userId, String content) throws EntityNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    LOGGER.info("[saveComment] 해당 게시물을 찾을 수 없음");
                    return new EntityNotFoundException("해당 게시물이 없습니다.");
                });

        Comment comment = Comment.builder()
                .content(content)
                .writer(userRepository.findById(userId)
                        .orElseThrow(() -> {
                            LOGGER.info("[saveComment] 해당 사용자를 찾을 수 없음");
                            return new NoSuchElementException("해당 사용자가 없습니다.");
                        }))
                .board(board)
                .build();
        commentRepository.save(comment);
        LOGGER.info("[saveComment] 댓글 저장, 게시글 ID: {}, 댓글 ID: {}", boardId, comment.getId());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId){
        Comment comment= (Comment) commentRepository.findByBoardId(commentId)
                .orElseThrow(() ->{
                    LOGGER.info("[deleteComment] 해당 댓글을 찾을 수 없음");
                    return new EntityNotFoundException("해당 댓글이 없습니다.");
                });

        commentRepository.delete(comment);
        LOGGER.info("[deleteComment] 댓글이 삭제되었습니다. 댓글 ID: {}", commentId);
    }

}
