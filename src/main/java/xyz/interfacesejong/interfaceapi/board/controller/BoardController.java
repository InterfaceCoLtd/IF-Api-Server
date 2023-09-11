package xyz.interfacesejong.interfaceapi.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.board.dto.BoardDto;
import xyz.interfacesejong.interfaceapi.board.service.BoardService;
import xyz.interfacesejong.interfaceapi.user.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    public BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    // 글작성
    @PostMapping("/create")
    public ResponseEntity<BoardDto> create(@RequestBody Map<String, String> param) throws Exception {
        String title = param.get("title");
        String content = param.get("content");
        Long user_id = Long.parseLong(param.get("user_id"));

        BoardDto boardDto = new BoardDto(title, content, user_id);
        boardService.save(boardDto);

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

}
