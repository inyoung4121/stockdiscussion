package in.userservice.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostSummaryDTO {
    private Long id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private String userName;
}