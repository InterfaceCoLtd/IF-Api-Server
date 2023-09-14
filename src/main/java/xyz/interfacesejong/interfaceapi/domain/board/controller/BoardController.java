package xyz.interfacesejong.interfaceapi.domain.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardDto;
import xyz.interfacesejong.interfaceapi.domain.board.service.BoardService;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.domain.user.service.UserService;
import xyz.interfacesejong.interfaceapi.global.util.FileUtils;

import java.util.List;
import java.util.Map;

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

}
