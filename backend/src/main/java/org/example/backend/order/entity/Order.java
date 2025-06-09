package org.example.backend.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.user.entity.User;
import org.example.backend.global.enums.OrderMethod;
import org.example.backend.global.enums.OrderStatus;
import org.example.backend.global.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String coinSymbol;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(precision = 19, scale = 8)
    private BigDecimal price; // 주문 당시 지정한 가격 (시장가일 경우 생략 가능)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;// BUY, SELL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderMethod orderMethod; // MARKET, LIMIT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus; // PENDING, COMPLETED, CANCELED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void updateOrder(BigDecimal newPrice, BigDecimal newQuantity) {
        this.price = newPrice;
        this.quantity = newQuantity;
    }

    public void markAsCompleted() {
        this.orderStatus = OrderStatus.COMPLETED;
    }

}