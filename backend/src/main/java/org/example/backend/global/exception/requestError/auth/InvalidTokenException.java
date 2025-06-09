package org.example.backend.global.exception.requestError.auth;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.AuthException;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super(message, ErrorCode.INVALID_TOKEN);
    }
}
