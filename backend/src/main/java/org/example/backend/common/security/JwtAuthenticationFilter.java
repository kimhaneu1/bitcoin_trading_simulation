package org.example.backend.common.security;

import org.example.backend.common.exception.auth.InvalidTokenException;
import org.example.backend.common.exception.auth.TokenMissingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth",
            "/swagger-ui",
            "/v3/api-docs",
            "/api/users"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (isWhitelisted(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        if (token == null) {
            throw new TokenMissingException("Authorization 헤더가 없습니다.");
        }

        jwtTokenProvider.validateToken(token);

        if (!isAccessToken(token)) {
            throw new InvalidTokenException("AccessToken만 허용됩니다.");
        }

        String type = jwtTokenProvider.getTokenType(token);
        if (!"access".equals(type)) {
            throw new InvalidTokenException("Access 토큰이 아닙니다.");
        }

        String email = jwtTokenProvider.getUserEmailFromToken(token);
        String role  = jwtTokenProvider.getRoleFromToken(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
                );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isWhitelisted(String uri) {
        return WHITE_LIST.stream().anyMatch(uri::startsWith);
    }

    private boolean isAccessToken(String token) {
        return "access".equals(JwtTokenProvider.getTokenType(token));
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
