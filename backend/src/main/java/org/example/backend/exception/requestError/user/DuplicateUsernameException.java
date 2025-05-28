package org.example.backend.exception.requestError.user;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.BusinessException;

public class DuplicateUsernameException extends BusinessException {

    public DuplicateUsernameException(String message) {
        super(message, ErrorCode.DUPLICATE_USERNAME);
    }
}