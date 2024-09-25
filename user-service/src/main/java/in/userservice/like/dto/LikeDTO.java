package in.userservice.like.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDTO {
    private Long id;
    private Long postId;
    private Long userId;
}
