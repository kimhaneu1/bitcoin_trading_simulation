package org.example.backend.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.backend.entity.enums.ExecutionType;
import org.example.backend.entity.enums.OrderStatus;
import org.example.backend.entity.enums.TradeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderGetResponseDto {
    private Long orderId;
    private String market;
    private TradeType tradeType;
    private ExecutionType executionType;
    private BigDecimal quantity;
    private BigDecimal priceAtOrderTime;
    private LocalDateTime orderedAt;
    private OrderStatus status;
    private BigDecimal totalAmount;
}