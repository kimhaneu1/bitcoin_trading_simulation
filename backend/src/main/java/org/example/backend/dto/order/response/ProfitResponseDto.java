package org.example.backend.dto.order.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProfitResponseDto {
    private BigDecimal profitRate; // 수익률 (%)
    private BigDecimal profitAmount; // 수익 금액 (총 차익)
}