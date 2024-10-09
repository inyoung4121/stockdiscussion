package in.userservice.posts.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
public class ResponsePostDTO {
    private Long id;
    private String userName;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
}
