package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.AuthException;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super(message, ErrorCode.INVALID_TOKEN);
    }
}
