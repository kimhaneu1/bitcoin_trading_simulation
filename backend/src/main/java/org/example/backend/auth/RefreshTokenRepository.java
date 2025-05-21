package org.example.backend.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<org.example.backend.auth.RefreshToken, String> {
    Optional<org.example.backend.auth.RefreshToken> findByJtiAndRevokedFalse(String jti);
    void deleteByJti(String jti);
}
