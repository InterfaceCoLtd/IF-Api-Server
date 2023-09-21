package xyz.interfacesejong.interfaceapi.domain.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Comment;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardDto;
import xyz.interfacesejong.interfaceapi.domain.board.service.BoardService;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.domain.user.service.UserService;
import xyz.interfacesejong.interfaceapi.global.util.FileUtils;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final FileService fileService;
    private final FileUtils fileUtils;

    public BoardController(BoardService boardService, UserService userService, FileService fileService, FileUtils fileUtils) {
        this.boardService = boardService;
        this.userService = userService;
        this.fileService = fileService;
        this.fileUtils = fileUtils;
    }

    // 글작성
    @PostMapping("/create")
    public ResponseEntity<BoardDto> create(@ModelAttribute BoardDto boardDto, @RequestParam List<MultipartFile> multipartFileList) throws Exception {

        boardService.save(boardDto, multipartFileList);

        return ResponseEntity.ok(boardDto);
    }

    // 전체 게시물 불러오기
    @GetMapping("/getAllBoards")
    public ResponseEntity<List<BoardDto>> getAllBoards() { return ResponseEntity.ok(boardService.getAllBoards()); }

    // 작성자 id로 게시물 불러오기
    @GetMapping("/findByUserId")
    public ResponseEntity<List<BoardDto>> findByUserId(@RequestParam("userId")Long id) throws Exception {
        return ResponseEntity.ok(boardService.findByUserId(id));
    }

    // 글 id로 게시물 불러오기
    @GetMapping("/findById")
    public ResponseEntity<BoardDto> findById(@RequestParam("id")Long id) throws Exception {
        return ResponseEntity.ok(boardService.findById(id));
    }

    // 글삭제
    @DeleteMapping("/delete")
    public ResponseEntity<BoardDto> delete(@RequestParam("id") Long id) throws Exception {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //글수정
    @PutMapping("/update")
    public ResponseEntity<BoardDto> update(@ModelAttribute BoardDto boardDto,@RequestParam List<MultipartFile> multipartFileList) throws Exception {
        BoardDto updatedBoardDto = boardService.update(boardDto, multipartFileList);

        return ResponseEntity.ok(updatedBoardDto);
    }

    //게시글 id로 댓글 리스트 불러오기
    @GetMapping("/getCommentsByBoardId")
    public ResponseEntity<Optional<List<Comment>>> findByBoardId(@RequestParam("boardId")Long id) throws Exception{
        return ResponseEntity.ok(boardService.getCommentsByBoardId(id));
    }

    //댓글 저장
    @PostMapping("/saveComment")
    public ResponseEntity<Comment> saveComment(
            @RequestParam("boardId") Long boardId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content) throws Exception {

        boardService.saveComment(boardId, userId, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시물에 댓글 삭제
    @DeleteMapping("/deleteComment")
    public ResponseEntity<Void> deleteComment(@RequestParam("commentId") Long commentId) {
        boardService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
