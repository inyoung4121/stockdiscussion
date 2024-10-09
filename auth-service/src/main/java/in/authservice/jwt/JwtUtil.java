package in.authservice.jwt;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Duration getRefreshTokenValidity() {
        return Duration.ofMillis(refreshExpiration);
    }

    //리프래쉬 토큰 생성
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    //리프래쉬 토큰으로 재발급
    public String generateNewAccessToken(String refreshToken) {
        String email = getEmailFromToken(refreshToken);
        return generateToken(email);
    }

    //리프래쉬 토큰으로 재발급
    public String generateNewRefreshToken(String refreshToken) {
        String email = getEmailFromToken(refreshToken);
        return generateRefreshToken(email);
    }

    // 토큰에서 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 토큰에서 만료 시간 추출
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 토큰에서 특정 클레임 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임 추출
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String token = null;

        // 1. Try to extract from header
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
            logger.debug("Token extracted from Authorization header");
            return token;
        }

        // 2. If not in header, try to extract from cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("stockJwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    logger.debug("Token extracted from cookie");
                    return token;
                }
            }
        }

        // 3. If token is still null, log the failure
        if (token == null) {
            logger.warn("No token found in request (neither in header nor in cookies)");
        }

        return token;
    }

    // 토큰 만료 여부 확인
    public Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // 토큰 생성
    public String generateToken(String email) {
        return doGenerateToken(Map.of(), email);
    }

    // 토큰 생성 내부 메소드
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long jwtExpirationInMs = 3600000;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰 유효성 검증
    public Boolean validateToken(String token, String email) {
        final String extractedEmail = getEmailFromToken(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    public String extractRefreshTokenFromCookies(String cookieHeader) {
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return null;
        }

        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            String[] parts = cookie.trim().split("=");
            if (parts.length == 2 && "refresh_token".equals(parts[0])) {
                return parts[1];
            }
        }

        return null;
    }
}