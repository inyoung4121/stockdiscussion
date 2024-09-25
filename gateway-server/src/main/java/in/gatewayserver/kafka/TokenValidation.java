package in.gatewayserver.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gatewayserver.dto.JWTValidationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class TokenValidation {

    private final KafkaTemplate<String, JWTValidationDTO> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public CompletableFuture<SendResult<String, JWTValidationDTO>> sendTokenValidationRequest(String token) {
        JWTValidationDTO jwtDTOForAuth = new JWTValidationDTO();
        jwtDTOForAuth.setToken(token);
        return kafkaTemplate.send("jwt-validation-request", null, jwtDTOForAuth);
    }

    @KafkaListener(topics = "jwt-validation-response", groupId = "gateway-jwt-validation")
    public void receiveTokenValidationResponse(String message) {
        try {
            JWTValidationDTO response = objectMapper.readValue(message, JWTValidationDTO.class);

            if (response.isValid()) {
                // 토큰이 유효한 경우의 처리
                System.out.println("Token is valid: " + response.getToken());
                // 여기에 추가적인 로직을 구현할 수 있습니다.
                // 예: 사용자 인증 상태 업데이트, 로그 기록 등
            } else {
                // 토큰이 유효하지 않은 경우의 처리
                System.out.println("Token is invalid: " + response.getToken());
                // 여기에 추가적인 로직을 구현할 수 있습니다.
                // 예: 인증 거부, 에러 응답 생성 등
            }
        } catch (JsonProcessingException e) {
            // JSON 파싱 오류 처리
            System.err.println("Error parsing JWT validation response: " + e.getMessage());
        }
    }
}
