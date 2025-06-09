package org.example.backend.global.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 유형: BUY(매수), SELL(매도)")
public enum OrderType {
    BUY,
    SELL
}
