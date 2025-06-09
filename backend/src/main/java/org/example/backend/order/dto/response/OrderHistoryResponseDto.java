package org.example.backend.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.global.enums.OrderStatus;
import org.example.backend.global.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 내역 응답 DTO")
public class OrderHistoryResponseDto {

    @Schema(description = "주문 ID", example = "101")
    private Long orderId;

    @Schema(description = "주문 생성 시간", example = "2024-06-09T14:32:00")
    private LocalDateTime createdAt;

    @Schema(description = "코인 심볼", example = "ETH")
    private String coinSymbol;

    @Schema(description = "주문 유형", example = "SELL")
    private OrderType orderType;

    @Schema(description = "주문 가격", example = "47000000")
    private BigDecimal price;

    @Schema(description = "주문 수량", example = "0.5")
    private BigDecimal quantity;

    @Schema(description = "총 주문 금액 (price * quantity)", example = "23500000")
    private BigDecimal totalAmount;

    @Schema(description = "주문 상태: PENDING 또는 COMPLETED", example = "COMPLETED")
    private OrderStatus orderStatus;
}
