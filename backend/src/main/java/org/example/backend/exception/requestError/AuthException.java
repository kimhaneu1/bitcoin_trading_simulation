package org.example.backend.exception.requestError;

import lombok.Getter;
import org.example.backend.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;

@Getter
public abstract class AuthException extends AuthenticationException {
    private final ErrorCode errorCode;

    protected AuthException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}