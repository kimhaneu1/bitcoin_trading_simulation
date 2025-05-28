package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.market.response.TickerResponseDto;
import org.example.backend.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticker")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TickerController {

    private final MarketService marketService;

    @GetMapping
    public ResponseEntity<TickerResponseDto> getTicker(@RequestParam(defaultValue = "KRW-BTC") String market) {
        TickerResponseDto response = marketService.getTicker(market);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
