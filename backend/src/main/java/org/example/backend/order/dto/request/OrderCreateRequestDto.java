package org.example.backend.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.global.enums.OrderMethod;
import org.example.backend.global.enums.OrderType;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "주문 생성 요청 DTO")
public class OrderCreateRequestDto {

    @Schema(description = "코인 심볼 (예: BTC, ETH)", example = "BTC")
    private String coinSymbol;

    @Schema(description = "주문 수량", example = "0.01")
    private BigDecimal quantity;

    @Schema(description = "지정가 주문일 경우 지정 가격", example = "50000000")
    private BigDecimal price;

    @Schema(description = "주문 유형: 매수(BUY) 또는 매도(SELL)", example = "BUY"
            , enumAsRef = false)
    private OrderType orderType;

    @Schema(description = "주문 방식: 시장가(MARKET) 또는 지정가(LIMIT)", example = "LIMIT"
            , enumAsRef = false)
    private OrderMethod orderMethod;
}