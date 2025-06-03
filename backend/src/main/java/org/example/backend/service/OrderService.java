package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.order.request.OrderCreateRequestDto;
import org.example.backend.dto.order.response.OrderCreateResponseDto;
import org.example.backend.dto.order.response.OrderGetResponseDto;
import org.example.backend.dto.order.response.ProfitResponseDto;
import org.example.backend.entity.Order;
import org.example.backend.entity.User;
import org.example.backend.entity.enums.ExecutionType;
import org.example.backend.entity.enums.OrderStatus;
import org.example.backend.entity.enums.TradeType;
import org.example.backend.exception.requestError.order.OrderNotFoundException;
import org.example.backend.repository.OrderRepository;
import org.example.backend.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto dto) {
        User user = securityUtils.getCurrentUser();
        OrderStatus status;

        if (dto.getExecutionType() == ExecutionType.MARKET) {
            status = OrderStatus.COMPLETED;
        }
        else {
            if (dto.getLimitPrice() == null) {
                throw new IllegalArgumentException("지정가 주문에는 limitPrice가 필요합니다.");
            }
            status = OrderStatus.PENDING;
        }

        Order order = Order.builder()
                .user(user)
                .market(dto.getMarket())
                .tradeType(dto.getTradeType())
                .executionType(dto.getExecutionType())
                .quantity(dto.getQuantity())
                .priceAtOrderTime(null)  // 아직 체결 안 됐으므로 null
                .orderedAt(LocalDateTime.now())
                .status(status)
                .build();

        Order savedOrder = orderRepository.save(order);

        return OrderCreateResponseDto.builder()
                .orderId(savedOrder.getId())
                .market(savedOrder.getMarket())
                .tradeType(savedOrder.getTradeType())
                .executionType(savedOrder.getExecutionType())
                .quantity(savedOrder.getQuantity())
                .orderedAt(savedOrder.getOrderedAt())
                .status(savedOrder.getStatus())
                .build();
    }


    @Transactional(readOnly = true)
    public List<OrderGetResponseDto> getOrdersForUser() {
        User user = securityUtils.getCurrentUser();
        List<Order> orders = orderRepository.findByUserOrderByOrderedAtDesc(user);

        return orders.stream()
                .map(order -> {
                    BigDecimal totalAmount = order.getPriceAtOrderTime().multiply(order.getQuantity());
                    return new OrderGetResponseDto(
                            order.getId(),
                            order.getMarket(),
                            order.getTradeType(),
                            order.getExecutionType(),
                            order.getQuantity(),
                            order.getPriceAtOrderTime(),
                            order.getOrderedAt(),
                            order.getStatus(),
                            totalAmount
                    );
                })
                .toList();
    }

    @Transactional
    public OrderCreateResponseDto updateOrder(Long orderId, OrderCreateRequestDto dto) {
        User user = securityUtils.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("본인의 주문만 수정할 수 있습니다.");
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("이미 체결된 주문은 수정할 수 없습니다.");
        }

        BigDecimal orderPrice;
        OrderStatus status;

        if (dto.getExecutionType() == ExecutionType.MARKET) {
            orderPrice = dto.getExecutionPrice();
            status = OrderStatus.COMPLETED;
        } else {
            if (dto.getLimitPrice() == null) {
                throw new IllegalArgumentException("지정가 주문에는 limitPrice가 필요합니다.");
            }
            orderPrice = dto.getLimitPrice();

            boolean isBuy = dto.getTradeType() == TradeType.BUY;
            boolean isSell = dto.getTradeType() == TradeType.SELL;

            if ((isBuy && dto.getLimitPrice().compareTo(dto.getExecutionPrice()) >= 0) ||
                    (isSell && dto.getLimitPrice().compareTo(dto.getExecutionPrice()) <= 0)) {
                status = OrderStatus.COMPLETED;
            } else {
                status = OrderStatus.PENDING;
            }
        }

        // 체결되면 사용자 자산 조정
        if (status == OrderStatus.COMPLETED) {
            BigDecimal totalAmount = orderPrice.multiply(dto.getQuantity());

            if (dto.getTradeType() == TradeType.BUY) {
                if (user.getBalance().compareTo(totalAmount) < 0) {
                    throw new IllegalStateException("잔액이 부족합니다.");
                }
                user.updateBalance(user.getBalance().subtract(totalAmount));
            } else {
                user.updateBalance(user.getBalance().add(totalAmount));
            }

            userRepository.save(user);
        }

        // 주문 내용 수정
        order.update(
                dto.getMarket(),
                dto.getTradeType(),
                dto.getExecutionType(),
                dto.getQuantity(),
                orderPrice,
                LocalDateTime.now(),
                status
        );

        Order updatedOrder = orderRepository.save(order);

        return new OrderResponseDto(
                updatedOrder.getId(),
                updatedOrder.getMarket(),
                updatedOrder.getTradeType(),
                updatedOrder.getExecutionType(),
                updatedOrder.getQuantity(),
                updatedOrder.getPriceAtOrderTime(),
                updatedOrder.getOrderedAt(),
                updatedOrder.getStatus()
        );
    }

    public ProfitResponseDto calculateProfit(Long orderId, BigDecimal currentPrice, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("자신의 주문만 확인할 수 있습니다.");
        }

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException("미체결 주문은 수익률을 계산할 수 없습니다.");
        }

        BigDecimal orderPrice = order.getPriceAtOrderTime();
        BigDecimal rate;
        BigDecimal diff;

        if (order.getTradeType() == TradeType.BUY) {
            diff = currentPrice.subtract(orderPrice);
            rate = diff.divide(orderPrice, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        } else {
            diff = orderPrice.subtract(currentPrice);
            rate = diff.divide(orderPrice, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }

        BigDecimal profit = diff.multiply(order.getQuantity());

        return ProfitResponseDto.builder()
                .profitRate(rate)
                .profitAmount(profit)
                .build();
    }

    @Transactional
    public void cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문이 없습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("자신의 주문만 취소할 수 있습니다.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("이미 체결된 주문은 취소할 수 없습니다.");
        }

        // 주문 삭제
        orderRepository.delete(order);
    }
}
