package org.example.backend.account;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.market.Market;
import org.example.backend.user.User;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal locked;

    @Column(nullable = false)
    private BigDecimal avgBuyPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    public void setUser(User user) {
        this.user = user;
    }

    public void updateQuantity(BigDecimal newQuantity) {
        this.quantity = newQuantity;
    }

    public void updateLocked(BigDecimal newLocked) {
        this.locked = newLocked;
    }
}
