package xyz.interfacesejong.interfaceapi.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.Schedule.service.ScheduleService;
import xyz.interfacesejong.interfaceapi.domain.board.domain.*;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardRequest;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardResponse;
import xyz.interfacesejong.interfaceapi.domain.board.dto.TitleDto;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.dto.UploadFileResponse;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileUtils;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final ScheduleService scheduleService;
    private final Logger LOGGER = LoggerFactory.getLogger(BoardService.class);

    @Transactional
    public BoardResponse save(BoardRequest boardRequest) {

        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(userRepository.findById(boardRequest.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다.")))
                .scheduleId(boardRequest.getScheduleId())
                .subjectId(boardRequest.getSubjectId()).build();

        boardRepository.save(board);

        if (boardRequest.getScheduleId() != null){
            scheduleService.updateBoardId(board.getScheduleId(), board.getId());
        }

        LOGGER.info("[save] : 게시글 저장, 게시글 ID {}", board.getId());
        return new BoardResponse(board);
    }

    @Transactional
    public BoardResponse saveFiles(BoardRequest boardRequest, List<MultipartFile> multipartFileList) {
        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(userRepository.findById(boardRequest.getUserId()).orElseThrow(() -> {
                    LOGGER.info("[saveFiles] : 해당 id를 가진 사용자가 없습니다, 사용자 id {}", boardRequest.getUserId());
                    return new NoSuchElementException();
                }))
                .subjectId(boardRequest.getSubjectId())
                .scheduleId(boardRequest.getScheduleId()).build();
        boardRepository.save(board);

        BoardResponse boardResponse = new BoardResponse(board);

        List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList, boardResponse);
        boardResponse.setFileNames(uploadFileList.stream()
                        .map(UploadFile::getSaveName)
                .collect(Collectors.toList()));
        fileService.saveFiles(board, uploadFileList);

        LOGGER.info("[saveFiles] : 게시글 저장 + 첨부파일 저장, 게시글 ID {}, 첨부파일 수 {}", board.getId(), uploadFileList.size());
        return boardResponse;
    }

    @Transactional
    public List<BoardResponse> getAllBoards() {
        List<BoardResponse> boardResponses = boardRepository.findAll()
                .stream()
                .map(board -> {
                    BoardResponse boardResponse = new BoardResponse(board);
                    boardResponse.setFileNames(fileService.getAllUploadFiles(board.getId())
                            .stream().map(UploadFileResponse::getSaveName).collect(Collectors.toList()));
                    return boardResponse;
                }).collect(Collectors.toList());

        LOGGER.info("[findAllBoards] : 모든 게시글 조회");
        return boardResponses;
    }

    @Transactional
    public List<TitleDto> getAllTitles() {
        LOGGER.info("[getAllTitles] : 모든 게시글의 제목 리스트 반환");
        return boardRepository.getAllTitles();
    }

    @Transactional
    public List<BoardResponse> findByUserId(Long id) throws EntityNotFoundException{
        List<BoardResponse> boardResponseList = new ArrayList<>();
        List<Board> boardList = boardRepository.findByWriterId(id)
                .orElseThrow(() -> {
                    LOGGER.info("[findById] : 작성자가 작성한 글이 없습니다, 작성자 id {}", id);
                    return new EntityNotFoundException("해당 작성자가 작성한 글이 없습니다.");
                });
        boardList.stream().forEach(board -> boardResponseList.add(new BoardResponse(board)));

        LOGGER.info("[findByUserId] : 작성자로 글 조회, 작성자 id {}, 게시글 개수 {}", id, boardList.size());
        return boardResponseList;
    }

    @Transactional
    public BoardResponse findById(Long id) {
        BoardResponse boardResponse = new BoardResponse(boardRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[findById] : 해당 id를 가진 게시글이 없습니다, 게시글 id {}", id);
                    return new EntityNotFoundException();
                }));

        LOGGER.info("[findById] : id로 글 조회, 게시글 id {}", id);
        return boardResponse;
    }

    @Transactional
    public BoardResponse findByScheduleId(Long id) throws EntityNotFoundException {
        BoardResponse boardResponse = new BoardResponse(boardRepository.findByScheduleId(id)
                .orElseThrow(() -> {
                    LOGGER.info("[findByScheduleId] : 해당 일정 id를 가진 게시글이 없습니다, 일정 id {}", id);
                    return new EntityNotFoundException();
                }));

        LOGGER.info("[findByScheduleId] : 일정 id로 글 조회, 일정 id {}", id);
        return boardResponse;
    }

    @Transactional
    public BoardResponse findBySubjectId(Long id) throws EntityNotFoundException {
        BoardResponse boardResponse = new BoardResponse(boardRepository.findBySubjectId(id)
                .orElseThrow(() -> {
                    LOGGER.info("[findBySubjectId] : 해당 투표 id를 가진 게시글이 없습니다, 투표 id {}", id);
                    return new EntityNotFoundException();
                }));

        LOGGER.info("[findBySubjectId] : 투표 id로 글 조회, 투표 id {}", id);
        return boardResponse;
    }

    @Transactional
    public void delete(Long id) throws EntityNotFoundException {
        boardRepository.deleteById(id);
        LOGGER.info("[delete] : 게시글 id로 글 삭제, 게시글 id {}", id);
    }

    @Transactional
    public BoardResponse update(Long id, BoardRequest updatedBoardRequest) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            LOGGER.info("[update] : 해당 id를 가진 게시글이 없습니다, 게시글 id {}", id);
            return new EntityNotFoundException();
        });
        board.update(updatedBoardRequest.getTitle(), updatedBoardRequest.getContent());
        fileService.deleteFilesByBoardId(id);

        LOGGER.info("[update] : 게시글을 수정, 게시글 id {}", id);
        return new BoardResponse(board);
    }

    @Transactional
    public BoardResponse updateFiles(Long id, BoardRequest updatedBoardRequest, List<MultipartFile> multipartFileList) throws EntityNotFoundException {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            LOGGER.info("[updateFiles] : 해당 id를 가진 게시글이 없습니다, 게시글 id {}", id);
            return new EntityNotFoundException();
        });
        board.update(updatedBoardRequest.getTitle(), updatedBoardRequest.getContent());

        BoardResponse boardResponse = new BoardResponse(board);

        fileService.deleteFilesByBoardId(id);
        List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList, boardResponse);
        fileService.saveFiles(board, uploadFileList);

        LOGGER.info("[updateFiles] : 게시글+첨부파일 수정, 게시글 id {}", id);
        return boardResponse;
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
