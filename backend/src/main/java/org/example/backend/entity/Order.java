package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.entity.enums.ExecutionType;
import org.example.backend.entity.enums.TradeType;
import org.example.backend.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "`order`")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String market;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, COMPLETED

    @Enumerated(EnumType.STRING)
    private TradeType tradeType; // BUY, SELL

    @Enumerated(EnumType.STRING)
    private ExecutionType executionType; // MARKET, LIMIT

    private BigDecimal quantity;
    private BigDecimal priceAtOrderTime;
    private LocalDateTime orderedAt;
}
