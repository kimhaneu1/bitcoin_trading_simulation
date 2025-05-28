package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.AuthException;

public class ExpiredTokenException extends AuthException {
    public ExpiredTokenException(String message) {
        super(message, ErrorCode.EXPIRED_TOKEN);
    }
}
