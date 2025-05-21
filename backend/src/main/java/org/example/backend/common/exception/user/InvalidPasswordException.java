package org.example.backend.common.exception.user;

import org.example.backend.common.exception.global.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException() {
        super("비밀번호를 다시 확인해주세요.", UserErrorCode.INVALID_PASSWORD);
    }
}
