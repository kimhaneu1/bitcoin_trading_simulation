package org.example.backend.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.backend.order.dto.request.OrderCreateRequestDto;
import org.example.backend.order.dto.request.OrderUpdateRequestDto;
import org.example.backend.order.dto.response.OrderHistoryResponseDto;
import org.example.backend.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "지정가 또는 시장가로 매수/매도 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderHistoryResponseDto> createOrder(@RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @Operation(summary = "주문 수정", description = "지정가 주문의 가격 및 수량을 수정합니다.")
    @PutMapping
    public ResponseEntity<OrderHistoryResponseDto> updateOrder(@RequestBody OrderUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.updateOrder(dto));
    }

    @Operation(summary = "주문 삭제", description = "사용자의 대기 중인 주문을 삭제합니다.")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "대기 주문 체결 시도", description = "사용자의 미체결 주문(PENDING)을 체결 가능한 경우 자동으로 체결합니다.")
    @PostMapping("/pending/resolve")
    public ResponseEntity<List<OrderHistoryResponseDto>> resolvePendingOrders() {
        return ResponseEntity.ok(orderService.resolvePendingOrders());
    }

    @Operation(summary = "주문 내역 조회", description = "현재 로그인한 사용자의 전체 주문 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderHistoryResponseDto>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }
}
