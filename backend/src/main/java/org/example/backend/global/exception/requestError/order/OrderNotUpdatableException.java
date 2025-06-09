package org.example.backend.global.exception.requestError.order;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class OrderNotUpdatableException extends BusinessException {
    public OrderNotUpdatableException(String message) {
        super(message, ErrorCode.ORDER_NOT_UPDATABLE);
    }
}
