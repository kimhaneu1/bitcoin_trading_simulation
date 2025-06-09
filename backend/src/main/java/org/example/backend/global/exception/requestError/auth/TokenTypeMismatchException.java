package org.example.backend.global.exception.requestError.auth;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.AuthException;

public class TokenTypeMismatchException extends AuthException {
    public TokenTypeMismatchException(String message) { super(message, ErrorCode.TOKEN_TYPE_MISMATCH_EXCEPTION); }
}
