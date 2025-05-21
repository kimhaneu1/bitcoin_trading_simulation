package org.example.backend.market.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickerResponseDto {
    private String market;

    @JsonProperty("trade_price")
    private double tradePrice;

    @JsonProperty("signed_change_rate")
    private double signedChangeRate;

    @JsonProperty("acc_trade_price_24h")
    private double accTradePrice24h;

    private long timestamp;
}

