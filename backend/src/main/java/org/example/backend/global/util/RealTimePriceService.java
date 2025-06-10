package org.example.backend.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RealTimePriceService {

    private final UpbitWebSocketClient upbitWebSocketClient;

    public BigDecimal getCurrentPrice(String coinSymbol) {
        return upbitWebSocketClient.getPrice(coinSymbol);
    }

    public void subscribe(String coinSymbol) {
        upbitWebSocketClient.subscribe(coinSymbol);
    }

}
