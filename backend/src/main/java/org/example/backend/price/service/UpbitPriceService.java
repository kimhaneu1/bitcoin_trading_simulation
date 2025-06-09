package org.example.backend.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpbitPriceService {

    private final WebClient webClient = WebClient.create("https://api.upbit.com");

    public BigDecimal getCurrentPrice(String coinSymbol) {
        String marketParam = "KRW-" + coinSymbol.toUpperCase();

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/ticker")
                            .queryParam("markets", marketParam)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .map(resultList -> {
                        Map<String, Object> result = resultList.get(0);
                        return new BigDecimal(result.get("trade_price").toString());
                    })
                    .block(); // 동기적으로 반환
        } catch (Exception e) {
            throw new RuntimeException("업비트 가격 조회 실패: " + e.getMessage(), e);
        }
    }
}
