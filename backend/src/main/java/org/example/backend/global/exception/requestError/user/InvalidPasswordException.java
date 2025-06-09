package org.example.backend.global.exception.requestError.user;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) { super(message, ErrorCode.INVALID_PASSWORD); }
}
