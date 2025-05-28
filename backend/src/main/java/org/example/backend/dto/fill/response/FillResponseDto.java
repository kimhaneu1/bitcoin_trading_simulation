package org.example.backend.dto.fill.response;

import org.example.backend.entity.enums.Side;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FillResponseDto {
    private Long id;
    private String market;
    private Side side;
    private BigDecimal price;
    private BigDecimal volume;
    private LocalDateTime createdAt;
    private Long orderId;
    private Long userId;
}
