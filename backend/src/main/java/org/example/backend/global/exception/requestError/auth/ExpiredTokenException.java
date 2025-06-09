package org.example.backend.global.exception.requestError.auth;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.AuthException;

public class ExpiredTokenException extends AuthException {
    public ExpiredTokenException(String message) {
        super(message, ErrorCode.EXPIRED_TOKEN);
    }
}
