package in.gatewayserver.dto;

import lombok.Data;

@Data
public class JWTValidationDTO {
    private String token;
    private boolean isValid;
}