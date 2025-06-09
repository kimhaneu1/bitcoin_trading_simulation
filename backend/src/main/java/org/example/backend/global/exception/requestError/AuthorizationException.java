package org.example.backend.global.exception.requestError;

import lombok.Getter;
import org.example.backend.global.exception.ErrorCode;
import org.springframework.security.access.AccessDeniedException;

@Getter
public abstract class AuthorizationException extends AccessDeniedException {
    private final ErrorCode errorCode;

    protected AuthorizationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}