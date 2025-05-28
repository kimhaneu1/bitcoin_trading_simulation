package org.example.backend.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "1000", "Invalid request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "1001", "Internal server error"),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "1100", "Invalid password"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1101", "User not found"),
    USER_LIST_EMPTY(HttpStatus.NOT_FOUND, "1102", "User list is empty"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "1103", "Email already exists"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "1104", "Username already exists"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "1200", "Invalid JWT token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "1201", "JWT token has expired"),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "1202", "JWT token is missing"),
    TOKEN_TYPE_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, "1203", "JWT token type mismatch"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "1204", "Access denied"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "1205", "Refresh token not found"),
    TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "1206", "Refresh token revoked"),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "1207", "Already log out"),

    MARKET_NOT_FOUND(HttpStatus.NOT_FOUND, "1300", "Market not found")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
