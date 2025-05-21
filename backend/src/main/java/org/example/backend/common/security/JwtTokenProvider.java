package org.example.backend.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.backend.auth.Role;
import org.example.backend.common.exception.auth.ExpiredTokenException;
import org.example.backend.common.exception.auth.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // 문자열로 주입

    @Value("${jwt.access-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenExpirationMs;

    /**
     * 서명용 Key 객체 생성 로직
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Authentication에서 Role 추출 (없으면 USER 기본)
     */
    private Role extractRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(granted -> {
                    String authStr = granted.getAuthority();
                    if (authStr.startsWith("ROLE_")) {
                        return Role.valueOf(authStr.substring(5));
                    }
                    return Role.USER;
                })
                .findFirst()
                .orElse(Role.USER);
    }

    // ==================== Access Token 생성 ====================

    /**
     * 인증 후 Authentication으로 바로 토큰 생성
     */
    public String createToken(Authentication auth) {
        return createToken(auth.getName(), extractRole(auth));
    }

    /**
     * 기본 USER 권한으로 토큰 생성 (명시적 Role 없이 호출할 때)
     */
    public String createToken(String email) {
        return createToken(email, Role.USER);
    }

    /**
     * 지정된 Role로 Access Token 생성
     */
    public String createToken(String email, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("type", "access")
                .claim("role", role.name()) // Role claim 추가
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==================== Refresh Token 생성 ====================

    /**
     * Authentication에서 Role을 추출해 Refresh Token 생성
     */
    public String createRefreshToken(Authentication auth) {
        return createRefreshToken(auth.getName(), extractRole(auth));
    }

    /**
     * 기본 USER 권한으로 Refresh Token 생성
     */
    public String createRefreshToken(String email) {
        return createRefreshToken(email, Role.USER);
    }

    /**
     * 지정된 Role로 Refresh Token 생성
     */
    public String createRefreshToken(String email, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .claim("role", role.name()) // Role claim 추가
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==================== 토큰 검증 ====================

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        } catch (Exception e) {
            throw new InvalidTokenException("토큰 검증 중 알 수 없는 오류가 발생했습니다.");
        }
    }

    // ==================== 토큰 정보 추출 ====================

    public String getUserEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("type", String.class);
    }

    public String getRoleFromToken(String token) { // 추가: Role claim 추출
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
