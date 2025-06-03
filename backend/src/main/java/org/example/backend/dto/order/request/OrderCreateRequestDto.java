package org.example.backend.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.entity.enums.ExecutionType;
import org.example.backend.entity.enums.TradeType;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {
    @NotNull
    private String market;

    @NotNull
    private TradeType tradeType;

    @NotNull
    private ExecutionType executionType;

    @NotNull
    private BigDecimal quantity;

    // LIMIT일 경우 필수, MARKET일 경우 null 가능
    private BigDecimal limitPrice;
}