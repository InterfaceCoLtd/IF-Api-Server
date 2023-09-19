package xyz.interfacesejong.interfaceapi.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.comment.domain.Comment;
import xyz.interfacesejong.interfaceapi.domain.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    //게시글 id로 댓글 리스트 불러오기
    @GetMapping("/findCommentsByBoardId")
    public ResponseEntity<List<Comment>> findById(@RequestParam("Boardid")Long id) throws Exception {
        return ResponseEntity.ok(commentService.findCommentsByBoardId(id));
    }

    //댓글 저장
    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment){
        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }
    //댓글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void>  deleteComment(Long id){
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
