package org.example.backend.exception.user;

import org.example.backend.exception.global.BusinessException;

public class DuplicateUsernameException extends BusinessException {

    public DuplicateUsernameException() {
        super("이미 존재하는 닉네임입니다.", UserErrorCode.DUPLICATE_USERNAME);
    }
}