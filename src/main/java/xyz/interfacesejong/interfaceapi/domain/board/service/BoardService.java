package xyz.interfacesejong.interfaceapi.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.domain.*;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardRequest;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardResponse;
import xyz.interfacesejong.interfaceapi.domain.board.dto.TitleDto;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileUtils;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);

    @Transactional
    public Board save(BoardRequest boardRequest) {

        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(userRepository.findById(boardRequest.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다.")))
                .scheduleId(boardRequest.getScheduleId())
                .subjectId(boardRequest.getSubjectId()).build();

        boardRepository.save(board);
        return board;
    }

    @Transactional
    public Board saveFiles(BoardRequest boardRequest, List<MultipartFile> multipartFileList) throws Exception {
        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(userRepository.findById(boardRequest.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다.")))
                .subjectId(boardRequest.getSubjectId())
                .scheduleId(boardRequest.getScheduleId()).build();
        boardRepository.save(board);

        List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList);
        fileService.saveFiles(board, uploadFileList);
        return board;
    }

    @Transactional
    public List<BoardResponse> getAllBoards() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardResponse> boardResponseList = new ArrayList<>();
        boardList.stream().forEach(board -> boardResponseList.add(new BoardResponse(board)));
        return boardResponseList;
        //LOGGER.info("[findAllBoards] : 모든 게시글 조회");
    }

    @Transactional
    public List<TitleDto> getAllTitles() {
        return boardRepository.getAllTitles();
    }

    @Transactional
    public List<BoardResponse> findByUserId(Long id) throws EntityNotFoundException{
        List<BoardResponse> boardResponseList = new ArrayList<>();
        List<Board> boardList = boardRepository.findByWriterId(id).get();
        boardList.stream().forEach(board -> boardResponseList.add(new BoardResponse(board)));

        return boardResponseList;
    }

    @Transactional
    public BoardResponse findById(Long id) throws EntityNotFoundException {
        BoardResponse boardResponse = new BoardResponse(boardRepository.findById(id).get());

        return boardResponse;
    }

    @Transactional
    public BoardResponse findByScheduleId(Long id) throws EntityNotFoundException {
        BoardResponse boardResponse = new BoardResponse(boardRepository.findByScheduleId(id).get());

        return boardResponse;
    }

    @Transactional
    public BoardResponse findBySubjectId(Long id) throws EntityNotFoundException {
        BoardResponse boardResponse = new BoardResponse(boardRepository.findBySubjectId(id).get());

        return boardResponse;
    }

    @Transactional
    public void delete(Long id) throws EntityNotFoundException {
        boardRepository.deleteById(id);
    }

    @Transactional
    public BoardResponse update(Long id, BoardRequest updatedBoardRequest) throws EntityNotFoundException {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));
        board.update(updatedBoardRequest.getTitle(), updatedBoardRequest.getContent());
        fileService.deleteFilesByBoardId(id);
        return new BoardResponse(board);
    }

    @Transactional
    public BoardResponse updateFiles(Long id, BoardRequest updatedBoardRequest, List<MultipartFile> multipartFileList) throws EntityNotFoundException {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));
        board.update(updatedBoardRequest.getTitle(), updatedBoardRequest.getContent());

        fileService.deleteFilesByBoardId(id);
        List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList);
        fileService.saveFiles(board, uploadFileList);

        return new BoardResponse(board);
    }

    //게시글 id로 댓글 리스트 불러오기
    @Transactional
    public Optional<List<Comment>> getCommentsByBoardId(Long boardId) throws EntityNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다."));

        log.info("[getCommentsByBoardId] 댓글 리스트 조회,게시글 ID: {}", boardId);
        return commentRepository.findByBoardId(boardId);
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
        log.info("[saveComment] 댓글 저장, 게시글 ID: {}, 댓글 ID: {}", boardId, comment.getId());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId){
        Comment comment= (Comment) commentRepository.findByBoardId(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 없습니다."));

        commentRepository.delete(comment);
        log.info("[deleteComment] 댓글이 삭제되었습니다. 댓글 ID: {}", commentId);
    }

}
