package org.example.backend.common.exception.market;

import org.example.backend.common.exception.global.BusinessException;

public class MarketNotFoundException extends BusinessException {
    public MarketNotFoundException(String message) {
        super(message, MarketErrorCode.MARKET_NOT_FOUND);
    }
}