package org.example.backend.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProfitRequestDto {
    @NotNull
    private Long orderId;

    @NotNull
    private BigDecimal currentPrice;
}