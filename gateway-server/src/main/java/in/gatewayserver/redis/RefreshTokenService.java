package in.gatewayserver.redis;

import com.example.jwttest.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public String createRefreshToken(String userId) {
        String refreshToken = jwtUtil.generateRefreshToken(userId);
        redisTemplate.opsForValue().set(
                "refresh_token:" + userId,
                refreshToken
        );
        return refreshToken;
    }

    public String getUserIdByRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get("refresh_token:" + refreshToken);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh_token:" + userId);
    }
}