package org.example.backend.order.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.global.util.CalculationUtil;
import org.example.backend.global.util.RealTimePriceService;
import org.example.backend.order.dto.request.OrderCreateRequestDto;
import org.example.backend.order.dto.request.OrderUpdateRequestDto;
import org.example.backend.order.dto.response.OrderHistoryResponseDto;
import org.example.backend.asset.entity.Asset;
import org.example.backend.order.entity.Order;
import org.example.backend.user.entity.User;
import org.example.backend.global.enums.OrderMethod;
import org.example.backend.global.enums.OrderStatus;
import org.example.backend.global.enums.OrderType;
import org.example.backend.global.exception.requestError.asset.AssetNotFoundException;
import org.example.backend.global.exception.requestError.order.*;
import org.example.backend.asset.repository.AssetRepository;
import org.example.backend.order.repository.OrderRepository;
import org.example.backend.global.security.core.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final SecurityUtils securityUtils;
    private final RealTimePriceService realTimePriceService;

    public OrderHistoryResponseDto createOrder(OrderCreateRequestDto dto) {
        User user = getCurrentUser();
        BigDecimal price = determineOrderPrice(dto);
        realTimePriceService.subscribe(dto.getCoinSymbol());
        Order order = buildOrder(dto, user, price);
        return toDto(orderRepository.save(order));
    }

    public OrderHistoryResponseDto updateOrder(OrderUpdateRequestDto dto) {
        User user = getCurrentUser();
        Order order = getOrderIfOwned(dto.getOrderId(), user);
        validateOrderUpdatable(order);
        order.updateOrder(dto.getPrice(), dto.getQuantity());
        return toDto(order);
    }

    public void deleteOrder(Long orderId) {
        User user = getCurrentUser();
        Order order = getOrderIfOwned(orderId, user);
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderNotDeletableException("체결 완료된 주문은 삭제할 수 없습니다.");
        }
        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryResponseDto> getMyOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toDto)
                .toList();
    }

    public List<OrderHistoryResponseDto> resolvePendingOrders() {
        User user = getCurrentUser();
        List<Order> pendingOrders = orderRepository.findByUserAndOrderStatus(user, OrderStatus.PENDING);
        List<OrderHistoryResponseDto> resolved = new ArrayList<>();

        for (Order order : pendingOrders) {
            BigDecimal currentPrice = realTimePriceService.getCurrentPrice(order.getCoinSymbol());
            if (isOrderExecutable(order.getOrderMethod(), order.getOrderType(), order.getPrice(), currentPrice)) {
                executeOrder(user, order);
                order.markAsCompleted();
                resolved.add(toDto(order));
            }
        }
        return resolved;
    }

    private void executeOrder(User user, Order order) {
        if (order.getOrderType() == OrderType.BUY) {
            executeBuyOrder(user, order);
        } else {
            executeSellOrder(user, order);
        }
    }

    private void executeBuyOrder(User user, Order order) {
        BigDecimal orderAmount = CalculationUtil.calculateOrderAmount(order.getPrice(), order.getQuantity());
        if (user.getKrwBalance().compareTo(orderAmount) < 0) {
            throw new InsufficientBalanceException();
        }

        user.updateKrwBalance(orderAmount.negate());
        Asset asset = getOrCreateAsset(user, order.getCoinSymbol());

        BigDecimal prevTotal = asset.getQuantity().multiply(asset.getAvgBuyPrice());
        BigDecimal newQuantity = asset.getQuantity().add(order.getQuantity());
        BigDecimal newTotal = prevTotal.add(orderAmount);

        asset.updateQuantity(newQuantity);
        asset.updateAvgBuyPrice(CalculationUtil.calculateAverageBuyPrice(newTotal, newQuantity));
        assetRepository.save(asset);
    }

    private void executeSellOrder(User user, Order order) {
        BigDecimal orderAmount = CalculationUtil.calculateOrderAmount(order.getPrice(), order.getQuantity());
        Asset asset = assetRepository.findByUserAndCoinSymbol(user, order.getCoinSymbol())
                .orElseThrow(AssetNotFoundException::new);

        if (asset.getQuantity().compareTo(order.getQuantity()) < 0) {
            throw new InsufficientAssetException();
        }

        asset.updateQuantity(asset.getQuantity().subtract(order.getQuantity()));
        user.updateKrwBalance(orderAmount);
        assetRepository.save(asset);
    }

    private Asset getOrCreateAsset(User user, String coinSymbol) {
        return assetRepository.findByUserAndCoinSymbol(user, coinSymbol)
                .orElseGet(() -> Asset.builder()
                        .user(user)
                        .coinSymbol(coinSymbol)
                        .quantity(BigDecimal.ZERO)
                        .avgBuyPrice(BigDecimal.ZERO)
                        .build());
    }

    private BigDecimal determineOrderPrice(OrderCreateRequestDto dto) {
        return dto.getOrderMethod() == OrderMethod.MARKET
                ? realTimePriceService.getCurrentPrice(dto.getCoinSymbol())
                : dto.getPrice();
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    private Order getOrderIfOwned(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderNotDeletableException("본인의 주문만 처리할 수 있습니다.");
        }
        return order;
    }

    private void validateOrderUpdatable(Order order) {
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderNotUpdatableException("체결이 완료된 주문은 수정할 수 없습니다.");
        }
        if (order.getOrderMethod() != OrderMethod.LIMIT) {
            throw new OrderNotUpdatableException("시장가 주문은 수정할 수 없습니다.");
        }
    }

    private boolean isOrderExecutable(OrderMethod method, OrderType type, BigDecimal limitPrice, BigDecimal currentPrice) {
        return switch (method) {
            case MARKET -> true;
            case LIMIT -> (type == OrderType.BUY)
                    ? currentPrice.compareTo(limitPrice) <= 0
                    : currentPrice.compareTo(limitPrice) >= 0;
        };
    }

    private Order buildOrder(OrderCreateRequestDto dto, User user, BigDecimal price) {
        return Order.builder()
                .user(user)
                .coinSymbol(dto.getCoinSymbol())
                .orderType(dto.getOrderType())
                .orderMethod(dto.getOrderMethod())
                .orderStatus(OrderStatus.PENDING)
                .quantity(dto.getQuantity())
                .price(price)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private OrderHistoryResponseDto toDto(Order order) {
        return OrderHistoryResponseDto.builder()
                .orderId(order.getId())
                .coinSymbol(order.getCoinSymbol())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .totalAmount(CalculationUtil.calculateOrderAmount(order.getPrice(), order.getQuantity()))
                .orderType(order.getOrderType())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
