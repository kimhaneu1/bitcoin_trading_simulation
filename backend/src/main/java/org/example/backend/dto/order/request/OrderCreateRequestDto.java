package org.example.backend.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.entity.enums.OrderMethod;
import org.example.backend.entity.enums.OrderType;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequestDto {
    private String coinSymbol;

    private BigDecimal quantity;

    private BigDecimal price; // 지정가 주문에만 사용됨

    private OrderType orderType;

    private OrderMethod orderMethod;
}
