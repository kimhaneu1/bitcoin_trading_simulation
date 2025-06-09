package org.example.backend.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 수정 요청 DTO")
public class OrderUpdateRequestDto {

    @Schema(description = "수정할 주문 ID", example = "123")
    private Long orderId;

    @Schema(description = "변경할 가격", example = "51000000")
    private BigDecimal price;

    @Schema(description = "변경할 수량", example = "0.02")
    private BigDecimal quantity;
}
