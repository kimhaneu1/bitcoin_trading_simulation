package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.order.request.OrderCreateRequestDto;
import org.example.backend.dto.order.response.OrderCreateResponseDto;
import org.example.backend.dto.order.request.ProfitRequestDto;
import org.example.backend.dto.order.response.OrderGetResponseDto;
import org.example.backend.dto.order.response.ProfitResponseDto;
import org.example.backend.entity.User;
import org.example.backend.security.SecurityUtils;
import org.example.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(@Valid @RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderGetResponseDto>> getOrders() {
        return ResponseEntity.ok(orderService.getOrdersForUser());
    }

    @PostMapping("/profit")
    public ResponseEntity<ProfitResponseDto> calculateProfit(
            @Valid @RequestBody ProfitRequestDto dto,
            @AuthenticationPrincipal User user
    ) {
        ProfitResponseDto result = orderService.calculateProfit(dto.getOrderId(), dto.getCurrentPrice(), user);
        return ResponseEntity.ok(result);
    }
}