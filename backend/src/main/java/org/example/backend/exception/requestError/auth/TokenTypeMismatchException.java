package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.AuthException;

public class TokenTypeMismatchException extends AuthException {
    public TokenTypeMismatchException(String message) { super(message, ErrorCode.TOKEN_TYPE_MISMATCH_EXCEPTION); }
}
