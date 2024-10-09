package in.userservice.comments.service;

import in.userservice.comments.entity.Comment;
import in.userservice.comments.dto.CommentDTO;
import in.userservice.comments.repository.CommentsRepository;
import in.userservice.newsfeed.service.NewsfeedService;
import in.userservice.posts.entity.Post;
import in.userservice.posts.exception.PostNotFoundException;
import in.userservice.posts.repository.PostsRepository;
import in.userservice.user.entity.User;
import in.userservice.user.exception.UserNotFoundException;
import in.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final NewsfeedService newsfeedService;


    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentsRepository.findByPostIdOrderByCreatedAtDesc(postId).stream()
                .map(CommentDTO::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public CommentDTO addComment(Long postId, Long userId, String content) {
        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .contents(content)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentsRepository.save(comment);

        newsfeedService.handleCommentEvent(comment);
        return CommentDTO.from(savedComment);
    }

}
