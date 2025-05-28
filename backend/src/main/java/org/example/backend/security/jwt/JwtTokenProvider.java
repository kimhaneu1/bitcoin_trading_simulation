package org.example.backend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.entity.RefreshToken;
import org.example.backend.exception.requestError.auth.ExpiredTokenException;
import org.example.backend.exception.requestError.auth.InvalidTokenException;
import org.example.backend.exception.requestError.auth.TokenMissingException;
import org.example.backend.exception.requestError.auth.TokenTypeMismatchException;
import org.example.backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Jws<Claims> parseClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new TokenMissingException("토큰이 존재하지 않습니다.");
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new InvalidTokenException("잘못된 토큰입니다.");
        }
    }

    public String createAccessToken(String email, String role) {
        Date now = Date.from(Instant.now());
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateAccessToken(String token) {
        Jws<Claims> claims = parseClaims(token);
        if (!"access".equals(claims.getBody().get("type", String.class))) {
            throw new TokenTypeMismatchException("Access 토큰이 아닙니다.");
        }
    }

    @Transactional
    public String createRefreshToken(String email, String role) {
        String jti = UUID.randomUUID().toString();
        Date now = Date.from(Instant.now());
        Date expiry = new Date(now.getTime() + refreshTokenExpirationMs);

        String token = Jwts.builder()
                .setId(jti)
                .setSubject(email)
                .claim("role", role)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.save(new RefreshToken(jti, email, expiry.toInstant(), false));
        return token;
    }

    public void validateRefreshToken(String token) {
        Jws<Claims> claims = parseClaims(token);
        if (!"refresh".equals(claims.getBody().get("type", String.class))) {
            throw new TokenTypeMismatchException("Refresh 토큰이 아닙니다.");
        }
        String jti = claims.getBody().getId();
        refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                .orElseThrow(() -> new TokenMissingException("DB에 RefreshToken 없음"));
    }

    public String getJtiFromToken(String token) {
        return parseClaims(token).getBody().getId();
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).getBody().get("role", String.class);
    }

    public Instant getExpirationDateFromToken(String token) {
        return parseClaims(token).getBody().getExpiration().toInstant();
    }
}
