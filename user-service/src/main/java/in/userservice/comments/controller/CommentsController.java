package in.userservice.comments.controller;

import in.userservice.comments.dto.CommentDTO;
import in.userservice.comments.dto.CommentRequest;
import in.userservice.comments.service.CommentService;
import in.userservice.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentsController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/comments/{postId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/api/comments/{postId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId,
                                                 @RequestBody CommentRequest request,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = Long.valueOf(jwtUtil.getEmailFromToken(token));
        CommentDTO commentDTO = commentService.addComment(postId, userId, request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDTO);
    }
}
