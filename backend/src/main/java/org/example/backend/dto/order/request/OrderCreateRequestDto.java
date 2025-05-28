package org.example.backend.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.entity.enums.OrderType;
import org.example.backend.entity.enums.Side;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {
    @NotNull private Long marketId;
    @NotNull private BigDecimal price;
    @NotNull private BigDecimal volume;
    @NotNull private OrderType orderType;
    @NotNull private Side side;
}