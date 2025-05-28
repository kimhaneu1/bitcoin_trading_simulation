package org.example.backend.security;

public class AuthWhitelist {
    public static final String[] NO_AUTH_PATHS = {
            "/api/auth/**",      // 로그인/회원가입 등
            "/swagger-ui/**",    // Swagger UI
            "/v3/api-docs/**",   // OpenAPI docs
            "/api/users"         // 사용자 가입
    };
}
