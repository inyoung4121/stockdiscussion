package in.userservice.user.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.userservice.jwt.JwtUtil;
import in.userservice.user.dto.JWTValidationDTO;
import in.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenValidation {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, JWTValidationDTO> kafkaTemplate;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @KafkaListener(topics = "jwt-validation-request", groupId = "user-jwt-validation")
    public void listenJwtValidateRequests(String message) {
        try {
            JWTValidationDTO request = objectMapper.readValue(message, JWTValidationDTO.class);
            log.info("Received JWT validation request for token: {}", request.getToken());

            String token = request.getToken();

            String email = jwtUtil.getEmailFromToken(token);

            userService.getUser(email);

            boolean isValid = jwtUtil.validateToken(request.getToken(),email);

            JWTValidationDTO response = new JWTValidationDTO();
            response.setToken(request.getToken());
            response.setValid(isValid);

            kafkaTemplate.send("jwt-validation-response", response);
            log.info("Sent JWT validation response for token: {}", request.getToken());
        } catch (Exception e) {
            log.error("Error processing JWT validation request: {}", e.getMessage(), e);
        }
    }
}