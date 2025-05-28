package org.example.backend.dto.fill.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.backend.entity.enums.Side;

import java.math.BigDecimal;

public class FillCreateDto {
    @NotBlank
    private String market;

    @NotNull
    private Side side;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal volume;

    @NotNull
    private Long orderId;
}
