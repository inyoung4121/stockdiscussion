package in.userservice.posts.dto;

import in.userservice.posts.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailDTO {
    private Long id;
    private String title;
    private String contents;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    public static PostDetailDTO from(Post post) {
        return PostDetailDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .userName(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .build();
    }
}