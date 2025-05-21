package org.example.backend.common.exception.global;

import org.example.backend.common.exception.BaseErrorCode;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final BaseErrorCode errorCode;

    protected BusinessException(String message, BaseErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}