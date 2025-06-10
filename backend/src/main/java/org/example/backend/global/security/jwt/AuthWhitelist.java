package org.example.backend.global.security.jwt;

public class AuthWhitelist {
    public static final String[] NO_AUTH_PATHS = {
            "/api/auth/login",      // 로그인
            "/swagger-ui/**",    // Swagger UI
            "/v3/api-docs/**",   // OpenAPI docs
    };
}
