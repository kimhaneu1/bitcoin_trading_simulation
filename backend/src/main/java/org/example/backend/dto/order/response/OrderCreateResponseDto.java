package org.example.backend.dto.order.response;

import lombok.Builder;
import org.example.backend.entity.enums.ExecutionType;
import org.example.backend.entity.enums.OrderStatus;
import org.example.backend.entity.enums.TradeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public class OrderCreateResponseDto {
    private Long orderId;
    private String market;
    private TradeType tradeType;
    private ExecutionType executionType;
    private BigDecimal quantity;
    private LocalDateTime orderedAt;
    private OrderStatus status;
}
