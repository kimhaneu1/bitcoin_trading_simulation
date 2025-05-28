package org.example.backend.exception.requestError.market;

import org.example.backend.exception.ErrorCode;
import org.example.backend.exception.requestError.BusinessException;

public class MarketNotFoundException extends BusinessException {
    public MarketNotFoundException(String message) { super(message, ErrorCode.MARKET_NOT_FOUND); }
}