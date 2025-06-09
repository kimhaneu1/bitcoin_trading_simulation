package org.example.backend.global.exception.requestError.auth;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.AuthException;

public class RefreshTokenNotFoundException extends AuthException {
    public RefreshTokenNotFoundException(String message) {
        super(message, ErrorCode.TOKEN_NOT_FOUND);
    }
}