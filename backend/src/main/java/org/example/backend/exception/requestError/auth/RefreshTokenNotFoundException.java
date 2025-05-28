package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.AuthException;

public class RefreshTokenNotFoundException extends AuthException {
    public RefreshTokenNotFoundException(String message) {
        super(message, ErrorCode.TOKEN_NOT_FOUND);
    }
}