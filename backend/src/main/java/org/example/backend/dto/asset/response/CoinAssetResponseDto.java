package org.example.backend.dto.asset.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinAssetResponseDto {
    private String coinSymbol;
    private String coinName;
    private BigDecimal quantity;
    private BigDecimal evaluatedAmount;
    private BigDecimal profitRate;
    private BigDecimal holdingRatio;
}