package org.example.backend.order.dto.response;

import org.example.backend.fill.dto.response.FillResponseDto;
import org.example.backend.order.enums.OrderState;
import org.example.backend.order.enums.OrderType;
import org.example.backend.order.enums.Side;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    private Long id;
    private String market;
    private Side side;
    private OrderType orderType;
    private BigDecimal price;
    private BigDecimal volume;
    private OrderState state;
    private LocalDateTime createdAt;
    private List<FillResponseDto> fills;
}
