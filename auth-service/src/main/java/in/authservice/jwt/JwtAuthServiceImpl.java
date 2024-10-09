package in.authservice.jwt;


import envoy.service.auth.v3.AuthorizationGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import envoy.service.auth.v3.JwtAuthService.*;

@GrpcService
public class JwtAuthServiceImpl extends AuthorizationGrpc.AuthorizationImplBase {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Override
    public void check(CheckRequest request, StreamObserver<CheckResponse> responseObserver) {
        String token = extractToken(request);
        String path = request.getHttpRequest().getPath();

        CheckResponse.Builder responseBuilder = CheckResponse.newBuilder();
        HttpResponse.Builder httpResponseBuilder = HttpResponse.newBuilder();

        if (isAuthExempt(path)) {
            responseBuilder.setStatus(CheckResponse.Status.OK);
        } else {
            if (jwtUtil.isTokenExpired(token)) {
                responseBuilder.setStatus(CheckResponse.Status.OK);
            } else {
                // 인증 실패 시 리프레시 토큰 확인
                String refreshToken = extractRefreshToken(request);
                if (isRefreshTokenValid(refreshToken)) {
                    // 토큰 갱신
                    String newToken = jwtUtil.generateNewAccessToken(refreshToken);
                    String newRefreshToken = jwtUtil.generateNewRefreshToken(refreshToken);
                    updateRefreshTokenInRedis(refreshToken, newRefreshToken);

                    httpResponseBuilder.putHeaders("Set-Cookie", "jwt=" + newToken + "; HttpOnly; Secure; SameSite=Strict;Max-Age="+expiration);
                    httpResponseBuilder.putHeaders("Set-Cookie", "refresh_token=" + newRefreshToken + "; HttpOnly; Secure; SameSite=StrictMax-Age="+refreshExpiration);
                    responseBuilder.setStatus(CheckResponse.Status.OK)
                            .setHttpResponse(httpResponseBuilder.build());
                } else {
                    // 리프레시 토큰도 유효하지 않은 경우
                    httpResponseBuilder.putHeaders("x-redirect-url", "/user/login");
                    responseBuilder.setStatus(CheckResponse.Status.DENIED)
                            .setHttpResponse(httpResponseBuilder.build());
                }
            }
        }

        CheckResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String extractToken(CheckRequest request) {
        return request.getHttpRequest().getHeadersOrDefault("Authorization", "")
                .replace("Bearer ", "");
    }

    private String extractRefreshToken(CheckRequest request) {
        String cookieHeader = request.getHttpRequest().getHeadersOrDefault("cookie", "");
        return jwtUtil.extractRefreshTokenFromCookies(cookieHeader);
    }

    private boolean isRefreshTokenValid(String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get(refreshToken);
        return storedToken != null;
    }

    private void updateRefreshTokenInRedis(String oldToken, String newToken) {
        redisTemplate.delete(oldToken);
        redisTemplate.opsForValue().set(newToken, "valid", jwtUtil.getRefreshTokenValidity());
    }

    private boolean isAuthExempt(String path) {
        return path.startsWith("/api/auth/");
    }
}