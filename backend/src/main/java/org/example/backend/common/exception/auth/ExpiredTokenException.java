package org.example.backend.common.exception.auth;

import org.example.backend.common.exception.global.BusinessException;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException(String message) {
        super(message, AuthErrorCode.EXPIRED_TOKEN);
    }
}
