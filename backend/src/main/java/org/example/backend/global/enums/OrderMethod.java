package org.example.backend.global.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 방식: MARKET(시장가), LIMIT(지정가)")
public enum OrderMethod {
    MARKET,
    LIMIT
}