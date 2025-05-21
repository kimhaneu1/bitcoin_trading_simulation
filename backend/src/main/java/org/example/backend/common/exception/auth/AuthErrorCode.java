package org.example.backend.common.exception.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.example.backend.common.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "1200", "Invalid JWT token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "1201", "JWT token has expired"),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "1202", "JWT token is missing"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "1203", "Access denied");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
