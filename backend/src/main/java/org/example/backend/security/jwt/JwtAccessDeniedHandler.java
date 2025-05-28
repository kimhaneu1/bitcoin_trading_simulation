package org.example.backend.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {
        String message = accessDeniedException.getMessage();
        if (message == null || message.isBlank()) {
            message = "권한에서 예상 못한 오류";
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }
}