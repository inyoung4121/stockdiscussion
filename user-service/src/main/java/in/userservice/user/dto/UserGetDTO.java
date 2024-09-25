package in.userservice.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetDTO {
    private String email;
    private String name;
    private String profile;
    private String intro;
}
