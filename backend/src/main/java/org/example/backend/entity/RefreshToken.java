package org.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "jti", nullable = false, updatable = false)
    private String jti;           // JWT ID (UUID)

    @Column(name = "email", nullable = false, length = 255)
    private String email;         // 사용자 식별자 (이메일)

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;   // 만료일시

    @Column(name = "revoked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean revoked;      // 토큰 취소 여부

    public void revoke() {
        this.revoked = true;
    }
}
