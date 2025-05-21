package org.example.backend.common.exception.auth;

import org.example.backend.common.exception.global.BusinessException;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(message, AuthErrorCode.ACCESS_DENIED);
    }
}
