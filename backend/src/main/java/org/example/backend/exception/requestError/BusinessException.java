package org.example.backend.exception.requestError;

import lombok.Getter;
import org.example.backend.exception.ErrorCode;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}