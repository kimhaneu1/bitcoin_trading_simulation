package org.example.backend.global.exception.requestError.order;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class InsufficientAssetException extends BusinessException {
    public InsufficientAssetException() {
        super(ErrorCode.INSUFFICIENT_ASSET);
    }
}
