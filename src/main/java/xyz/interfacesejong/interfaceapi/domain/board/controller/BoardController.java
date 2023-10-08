package xyz.interfacesejong.interfaceapi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Comment;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardRequest;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardResponse;
import xyz.interfacesejong.interfaceapi.domain.board.dto.TitleDto;
import xyz.interfacesejong.interfaceapi.domain.board.service.BoardService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
@Tag(name = "Board", description = "게시글 API")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //@Timer
    @PostMapping()
    @Operation(summary = "글 작성", description = "새로운 글을 생성합니다.")
    public ResponseEntity<Board> create(@ModelAttribute BoardRequest boardRequest, @RequestParam(required = false) List<MultipartFile> multipartFileList) throws Exception {
        if(multipartFileList==null) return ResponseEntity.ok(boardService.save(boardRequest));
        else return ResponseEntity.ok(boardService.saveFiles(boardRequest, multipartFileList));
    }

    @Timer
    @GetMapping()
    @Operation(summary = "모든 글 조회", description = "모든 글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> getAllBoards() { return ResponseEntity.ok(boardService.getAllBoards()); }

    @Timer
    @GetMapping("/title")
    @Operation(summary = "모든 글 제목", description = "글 목록 조회 시 제목 리스트만 반환합니다.")
    public ResponseEntity<List<TitleDto>> getAllTitles() {
        return ResponseEntity.ok(boardService.getAllTitles());
    }

    @Timer
    @GetMapping("/user/{id}")
    @Operation(summary = "작성자 id로 글 조회", description = "작성자 id로 모든 글을 조회합니다.")
    public ResponseEntity<List<BoardResponse>> findByUserId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(boardService.findByUserId(id));
    }

    @Timer
    @GetMapping("/board/{id}")
    @Operation(summary = "게시글 조회", description = "글 id로 글을 조회합니다.")
    public ResponseEntity<BoardResponse> findById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @Timer
    @GetMapping("/schedule/{id}")
    @Operation(summary = "일정 id로 글 조회", description = "일정 id로 글을 조회합니다")
    public ResponseEntity<BoardResponse> findByScheduleId(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findByScheduleId(id));
    }

    @Timer
    @GetMapping("/subject/{id}")
    @Operation(summary = "투표 주제 id로 글 조회", description = "투표 주제 id로 글을 조회합니다")
    public ResponseEntity<BoardResponse> findBySubjectId(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findBySubjectId(id));
    }

    @Timer
    @DeleteMapping("/board/{id}")
    @Operation(summary = "글 삭제", description = "글 id로 게시글을 삭제합니다.")
    public ResponseEntity<BoardResponse> delete(@PathVariable Long id) {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@Timer
    @PutMapping("/board/{id}")
    @Operation(summary = "글 업데이트", description = "해당 id의 게시글을 수정합니다.")
    public ResponseEntity<BoardResponse> update(@PathVariable Long id, @ModelAttribute BoardRequest boardRequest, @RequestParam(required = false) List<MultipartFile> multipartFileList) {
        BoardResponse updatedBoardResponse;
        if(multipartFileList==null) updatedBoardResponse = boardService.update(id, boardRequest);
        else updatedBoardResponse = boardService.updateFiles(id, boardRequest, multipartFileList);
        return ResponseEntity.ok(updatedBoardResponse);
    }

    //게시글 id로 댓글 리스트 불러오기
    @Timer
    @GetMapping("/Comments")
    public ResponseEntity<Optional<List<Comment>>> findByBoardId(@RequestParam("boardId")Long id) throws Exception{
        return ResponseEntity.ok(boardService.getCommentsByBoardId(id));
    }

    //댓글 저장
    @Timer
    @PostMapping("/Comment/{id}")
    public ResponseEntity<Comment> saveComment(
            @PathVariable Long commentId,
            @RequestParam("boardId") Long boardId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content) throws Exception {

        boardService.saveComment(boardId, userId, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시물에 댓글 삭제
    @Timer
    @DeleteMapping("/Comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        boardService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
