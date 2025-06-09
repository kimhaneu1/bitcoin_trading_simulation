package org.example.backend.global.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상태: COMPLETED(체결 완료), PENDING(미체결)")
public enum OrderStatus {
    COMPLETED,
    PENDING
}