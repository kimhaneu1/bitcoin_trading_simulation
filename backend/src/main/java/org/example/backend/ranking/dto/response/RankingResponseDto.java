package org.example.backend.ranking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponseDto {
    private int rank;
    private String username;
    private BigDecimal totalAssetAmount;
    private String topCoin;
    private BigDecimal profitRate;
}
