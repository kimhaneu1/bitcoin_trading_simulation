package org.example.backend.global.exception.requestError.auth;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.AuthException;

public class TokenMissingException extends AuthException {
    public TokenMissingException(String message) {
        super(message, ErrorCode.TOKEN_MISSING);
    }
}

