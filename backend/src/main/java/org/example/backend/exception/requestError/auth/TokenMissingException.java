package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.AuthException;

public class TokenMissingException extends AuthException {
    public TokenMissingException(String message) {
        super(message, ErrorCode.TOKEN_MISSING);
    }
}

