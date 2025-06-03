package org.example.backend.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.repository.OrderRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PriceService priceService; // 현재가를 조회하는 서비스

    @Scheduled(fixedDelay = 500)
    public void processPendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);

        for (Order order : pendingOrders) {
            BigDecimal currentPrice = priceService.getCurrentPrice(order.getMarket());
            boolean shouldExecute = shouldExecuteOrder(order, currentPrice);

            if (shouldExecute) {
                executeOrder(order, currentPrice);
            }
        }
    }

    private boolean shouldExecuteOrder(Order order, BigDecimal currentPrice) {
        return switch (order.getTradeType()) {
            case BUY -> order.getPriceAtOrderTime().compareTo(currentPrice) >= 0;
            case SELL -> order.getPriceAtOrderTime().compareTo(currentPrice) <= 0;
        };
    }

    private void executeOrder(Order order, BigDecimal executionPrice) {
        BigDecimal totalAmount = executionPrice.multiply(order.getQuantity());

        if (order.getTradeType().name().equals("BUY")) {
            if (order.getUser().getBalance().compareTo(totalAmount) < 0) {
                log.warn("잔액 부족 - 주문 실행 불가: orderId={}", order.getId());
                return;
            }
            order.getUser().updateBalance(order.getUser().getBalance().subtract(totalAmount));
        } else {
            order.getUser().updateBalance(order.getUser().getBalance().add(totalAmount));
        }

        order.setStatus(OrderStatus.COMPLETED);
        userRepository.save(order.getUser());
        orderRepository.save(order);

        log.info("주문 체결 완료: orderId={}, price={}", order.getId(), executionPrice);
    }
}