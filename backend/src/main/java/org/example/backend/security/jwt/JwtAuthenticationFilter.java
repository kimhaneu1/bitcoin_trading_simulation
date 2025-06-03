package org.example.backend.security.jwt;

import org.example.backend.exception.requestError.auth.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.exception.requestError.auth.TokenMissingException;
import org.example.backend.security.AuthWhitelist;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        for (String path : AuthWhitelist.NO_AUTH_PATHS) {
            if (path.endsWith("/**")
                    ? uri.startsWith(path.replace("/**", ""))
                    : uri.equals(path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String bearer = request.getHeader("Authorization");
        if (bearer == null) {
            throw new TokenMissingException("토큰 없음");
        }
        if (!bearer.startsWith("Bearer ")) {
            throw new InvalidTokenException("잘못된 토큰 형식입니다.");
        }

        String token = bearer.substring(7);

        jwtTokenProvider.validateAccessToken(token);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}