package in.userservice.posts.service;


import in.userservice.newsfeed.service.NewsfeedService;
import in.userservice.posts.entity.Post;
import in.userservice.posts.dto.PagedPostResponse;
import in.userservice.posts.dto.PostDTO;
import in.userservice.posts.dto.PostDetailDTO;
import in.userservice.posts.dto.ResponsePostDTO;
import in.userservice.posts.exception.PostNotFoundException;
import in.userservice.posts.repository.PostsRepository;
import in.userservice.user.entity.User;
import in.userservice.user.dto.PostSummaryDTO;
import in.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final NewsfeedService newsfeedService;

    @Transactional
    public Post savePost(PostDTO postDTO) {
        User user = userRepository.findById(postDTO.getUserId()).get();
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .contents(postDTO.getContents())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        newsfeedService.handlePostEvent(post);
        return postsRepository.save(post);
    }

    @Transactional
    public Post updatePost(PostDTO updatedPost) {
        Optional<Post> existingPostOptional = postsRepository.findById(updatedPost.getId());
        if (existingPostOptional.isPresent()) {
            Post existingPost = existingPostOptional.get();
            existingPost.updateTitle(updatedPost.getTitle());
            existingPost.updateContents(updatedPost.getContents());
            return postsRepository.save(existingPost);
        } else {
            throw new RuntimeException("Post not found with id: " + updatedPost.getId());
        }
    }

    @Transactional(readOnly = true)
    public PostDetailDTO getPostById(Long id) {
        log.debug("Fetching post with id: {}", id);

        return postsRepository.findById(id)
                .map(PostDetailDTO::from)
                .orElseThrow(() -> {
                    log.warn("Attempted to fetch non-existent post with id: {}", id);
                    return new PostNotFoundException(id);
                });
    }

    public PagedPostResponse getRecentPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postsRepository.findAllByOrderByCreatedAtDesc(pageable);

        Page<ResponsePostDTO> postDTOs = posts.map(this::convertToDTO);
        return new PagedPostResponse(postDTOs);
    }


    private ResponsePostDTO convertToDTO(Post post) {
        return ResponsePostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .userName(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getRecentPostsByUser(Long userId, int limit) {
        return postsRepository.findTopNByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    private PostSummaryDTO convertToSummaryDTO(Post post) {
        return PostSummaryDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents().substring(0, Math.min(post.getContents().length(), 100)) + "...")
                .createdAt(post.getCreatedAt())
                .userName(post.getUser().getName())
                .build();
    }
}
