package org.example.backend.market.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketResponseDto {
    private Long id;
    private String symbol;
    private String koreanName;
}