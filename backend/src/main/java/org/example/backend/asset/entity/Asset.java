package org.example.backend.asset.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.user.entity.User;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String coinSymbol; // BTC, ETH, KRW 등

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal avgBuyPrice;

    public void updateQuantity(BigDecimal newQuantity) {
        this.quantity = newQuantity;
    }

    public void updateAvgBuyPrice(BigDecimal newAvgBuyPrice) {
        this.avgBuyPrice = newAvgBuyPrice;
    }
}
