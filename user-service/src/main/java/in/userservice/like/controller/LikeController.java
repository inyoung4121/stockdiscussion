package in.userservice.like.controller;


import in.userservice.jwt.JwtUtil;
import in.userservice.like.comm.LikeType;
import in.userservice.like.service.LikeService;
import in.userservice.posts.service.PostsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;
    private final JwtUtil jwtUtil;
    private final PostsService postsService;

    @GetMapping("/api/likes/post/{postId}")
    public ResponseEntity<Map<String, Object>> getPostLikeStatus(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = getUserIdFromJwtToken(request);
        boolean hasLiked = likeService.hasUserLiked(postId, userId,LikeType.POST);
        long likeCount = likeService.getLikeCount(postId,LikeType.POST);
        Map<String, Object> response = new HashMap<>();
        response.put("liked", hasLiked);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/likes/post/{postId}")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = getUserIdFromJwtToken(request);
        boolean isLinked = likeService.toggleLike(userId,postId,LikeType.POST);
        long likeCount = likeService.getLikeCount(postId,LikeType.POST);
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLinked);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/likes/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> getCommentLikeStatus(@PathVariable Long commentId, HttpServletRequest request) {
        Long userId = getUserIdFromJwtToken(request);
        boolean hasLiked = likeService.hasUserLiked(commentId, userId, LikeType.COMMENT);
        long likeCount = likeService.getLikeCount(commentId, LikeType.COMMENT);
        Map<String, Object> response = new HashMap<>();
        response.put("liked", hasLiked);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/likes/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> likeComment(@PathVariable Long commentId, HttpServletRequest request) {
        Long userId = getUserIdFromJwtToken(request);
        boolean isLiked = likeService.toggleLike(userId, commentId, LikeType.COMMENT);
        long likeCount = likeService.getLikeCount(commentId, LikeType.COMMENT);
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    public Long getUserIdFromJwtToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("stockJwtToken".equals(cookie.getName())) {
                    try {
                        return Long.valueOf(jwtUtil.getEmailFromToken(cookie.getValue()));
                    } catch (Exception e) {
                        log.error("Invalid token", e);
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
