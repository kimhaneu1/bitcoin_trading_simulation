package org.example.backend.market;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.exception.market.MarketNotFoundException;
import org.example.backend.market.dto.request.MarketCreateRequestDto;
import org.example.backend.market.dto.response.MarketResponseDto;
import org.example.backend.market.dto.response.TickerResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketService {
    private final MarketRepository marketRepository;

    @Transactional
    public MarketResponseDto createMarket(MarketCreateRequestDto request) {
        if (marketRepository.existsBySymbol(request.getSymbol())) {
            throw new IllegalArgumentException("이미 존재하는 마켓입니다.");
        }

        Market market = Market.builder()
                .symbol(request.getSymbol())
                .koreanName(request.getKoreanName())
                .build();

        Market saved = marketRepository.save(market);

        return MarketResponseDto.builder()
                .id(saved.getId())
                .symbol(saved.getSymbol())
                .koreanName(saved.getKoreanName())
                .build();
    }

    public List<MarketResponseDto> getAllMarkets() {
        return marketRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MarketResponseDto convertToDto(Market market) {
        return MarketResponseDto.builder()
                .id(market.getId())
                .symbol(market.getSymbol())
                .koreanName(market.getKoreanName())
                .build();
    }

    public MarketResponseDto getMarketBySymbol(String symbol) {
        Market market = marketRepository.findBySymbol(symbol)
                .orElseThrow(() -> new MarketNotFoundException("symbol: " + symbol));

        return MarketResponseDto.builder()
                .id(market.getId())
                .symbol(market.getSymbol())
                .koreanName(market.getKoreanName())
                .build();
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public TickerResponseDto getTicker(String market) {
        try {
            String url = "https://api.upbit.com/v1/ticker?markets=" + market;
            TickerResponseDto[] response = restTemplate.getForObject(url, TickerResponseDto[].class);
            return (response != null && response.length > 0) ? response[0] : null;
        } catch (Exception e) {
            return null;
        }
    }
}
