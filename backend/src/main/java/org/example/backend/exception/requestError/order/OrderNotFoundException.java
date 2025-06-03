package org.example.backend.exception.requestError.order;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.BusinessException;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException(String message) {
        super(message, ErrorCode.ORDER_NOT_FOUND);
    }
}
