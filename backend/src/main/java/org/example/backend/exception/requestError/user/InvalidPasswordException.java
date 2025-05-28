package org.example.backend.exception.requestError.user;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) { super(message, ErrorCode.INVALID_PASSWORD); }
}
