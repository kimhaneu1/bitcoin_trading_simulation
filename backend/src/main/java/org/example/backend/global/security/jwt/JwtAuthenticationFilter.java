package org.example.backend.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.ErrorResponse;
import org.example.backend.global.exception.requestError.auth.ExpiredTokenException;
import org.example.backend.global.exception.requestError.auth.InvalidTokenException;
import org.example.backend.global.exception.requestError.auth.TokenMissingException;
import org.example.backend.global.exception.requestError.auth.TokenTypeMismatchException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 인증 제외 경로 처리
        if (uri.equals("/api/auth/login") ||
                (method.equals("POST") && uri.equals("/api/users")) ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearer = request.getHeader("Authorization");

        try {
            if (bearer == null || bearer.isBlank()) {
                throw new TokenMissingException("Authorization 헤더가 없습니다.");
            }

            if (!bearer.startsWith("Bearer ")) {
                throw new InvalidTokenException("Bearer 타입이 아닙니다.");
            }

            String token = bearer.substring(7);
            jwtTokenProvider.validateAccessToken(token);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (InvalidTokenException | ExpiredTokenException | TokenTypeMismatchException e) {
            setErrorResponse(response, ErrorCode.INVALID_TOKEN, e.getMessage());
        } catch (TokenMissingException e) {
            setErrorResponse(response, ErrorCode.TOKEN_MISSING, e.getMessage());
        } catch (Exception e) {
            setErrorResponse(response, ErrorCode.I_DONT_KNOW, "예상치 못한 인증 오류");
        }

    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
