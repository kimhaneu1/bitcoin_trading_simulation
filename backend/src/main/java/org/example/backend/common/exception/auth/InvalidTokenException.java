package org.example.backend.common.exception.auth;

import org.example.backend.common.exception.global.BusinessException;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(message, AuthErrorCode.INVALID_TOKEN);
    }
}
