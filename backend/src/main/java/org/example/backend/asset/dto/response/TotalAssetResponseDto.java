package org.example.backend.asset.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalAssetResponseDto {
    @Schema(description = "총 보유 자산 (현금 + 코인 평가금액)", example = "12450000")
    private BigDecimal totalAssetAmount;

    @Schema(description = "보유 원화 잔액", example = "5000000")
    private BigDecimal krwBalance;

    @Schema(description = "보유 원화 비율 (%)", example = "40.0")
    private BigDecimal krwRatio;

    @Schema(description = "보유 코인 평가 금액", example = "7450000")
    private BigDecimal coinAssetAmount;

    @Schema(description = "보유 코인 비율 (%)", example = "60.0")
    private BigDecimal coinRatio;

    @Schema(description = "보유 중인 코인 자산 목록")
    private List<CoinAssetResponseDto> coinAssets;
}