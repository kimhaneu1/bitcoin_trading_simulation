package org.example.backend.exception.requestError.user;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.BusinessException;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException(String message) {
        super(message, ErrorCode.DUPLICATE_EMAIL);
    }
}