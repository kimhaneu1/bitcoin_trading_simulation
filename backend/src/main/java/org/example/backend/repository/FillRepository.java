package org.example.backend.repository;

import org.example.backend.entity.Fill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FillRepository extends JpaRepository<Fill, Long> {
    List<Fill> findByUserId(Long orderId);
}
