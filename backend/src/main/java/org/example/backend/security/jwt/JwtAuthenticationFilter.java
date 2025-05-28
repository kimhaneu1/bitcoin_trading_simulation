package org.example.backend.security.jwt;

import org.example.backend.exception.requestError.auth.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.exception.requestError.auth.TokenMissingException;
import org.example.backend.security.AuthWhitelist;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

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

        String email = jwtTokenProvider.getEmailFromToken(token);
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
}