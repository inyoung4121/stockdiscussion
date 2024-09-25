package in.userservice.posts.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
public class PostDTO {
    private Long id;
    private Long userId;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
}
