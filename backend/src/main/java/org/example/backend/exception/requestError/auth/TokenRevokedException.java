package org.example.backend.exception.requestError.auth;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.AuthException;

public class TokenRevokedException extends AuthException {
    public TokenRevokedException() {
        super("토큰이 이미 폐기되었습니다.", ErrorCode.TOKEN_REVOKED);
    }
}