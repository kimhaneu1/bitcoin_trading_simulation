package org.example.backend.dto.market.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MarketCreateRequestDto {

    @NotBlank
    private String symbol;

    @NotBlank
    private String koreanName;
}