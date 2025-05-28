package org.example.backend.repository;

import org.example.backend.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findBySymbol(String symbol);

    boolean existsBySymbol(String symbol);
}