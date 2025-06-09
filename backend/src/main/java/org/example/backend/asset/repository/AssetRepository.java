package org.example.backend.asset.repository;

import org.example.backend.asset.entity.Asset;
import org.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUser(User user);
    Optional<Asset> findByUserAndCoinSymbol(User user, String coinName);
}
