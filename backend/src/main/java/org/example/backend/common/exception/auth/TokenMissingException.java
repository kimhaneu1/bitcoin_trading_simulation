package org.example.backend.common.exception.auth;

import org.example.backend.common.exception.global.BusinessException;

public class TokenMissingException extends BusinessException {
    public TokenMissingException(String message) {
        super(message, AuthErrorCode.TOKEN_MISSING);
    }
}

