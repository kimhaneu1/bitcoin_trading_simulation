package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.auth.request.LoginRequestDto;
import org.example.backend.dto.auth.response.TokenResponseDto;
import org.example.backend.entity.RefreshToken;
import org.example.backend.exception.requestError.auth.AlreadyLogoutException;
import org.example.backend.exception.requestError.auth.RefreshTokenNotFoundException;
import org.example.backend.repository.RefreshTokenRepository;
import org.example.backend.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        String email = auth.getName();
        String role  = extractRole(auth);

        TokenResponseDto tokens = generateTokenDto(email, role);
        saveRefreshToken(tokens.getRefreshToken());
        return tokens;
    }

    @Transactional
    public TokenResponseDto refresh(String oldRefreshToken) {
        tokenProvider.validateRefreshToken(oldRefreshToken);

        String oldJti = tokenProvider.getJtiFromToken(oldRefreshToken);
        RefreshToken existing = refreshTokenRepository
                .findByJtiAndRevokedFalse(oldJti)
                .orElseThrow(() -> new RefreshTokenNotFoundException("DB 에서 Refresh Token 실종"));
        existing.revoke();
        refreshTokenRepository.save(existing);

        String email = tokenProvider.getEmailFromToken(oldRefreshToken);
        String role  = tokenProvider.getRoleFromToken(oldRefreshToken);

        TokenResponseDto tokens = generateTokenDto(email, role);
        saveRefreshToken(tokens.getRefreshToken());
        return tokens;
    }

    @Transactional
    public void logout(String refreshToken) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        int revokedCount = refreshTokenRepository.revokeAllByEmail(email);

        if (revokedCount == 0) {
            throw new AlreadyLogoutException("이미 로그아웃된 사용자입니다.");
        }
    }

    private TokenResponseDto generateTokenDto(String email, String role) {
        String accessToken  = tokenProvider.createAccessToken(email, role);
        String refreshToken = tokenProvider.createRefreshToken(email, role);
        return new TokenResponseDto(accessToken, refreshToken);
    }


    private void saveRefreshToken(String refreshToken) {
        String jti  = tokenProvider.getJtiFromToken(refreshToken);
        Instant expiry  = tokenProvider.getExpirationDateFromToken(refreshToken);
        String email  = tokenProvider.getEmailFromToken(refreshToken);

        RefreshToken entity = new RefreshToken(jti, email, expiry, false);
        refreshTokenRepository.save(entity);
    }

    private String extractRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .findFirst()
                .map(a -> a.substring("ROLE_".length()))
                .orElse("USER");
    }
}
