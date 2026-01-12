package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    List<PriceAlert> findByUserIdOrderByCreatedDateDesc(Integer userId);

    List<PriceAlert> findByUserIdAndIsActiveTrue(Integer userId);

    Optional<PriceAlert> findByUserIdAndProductIdAndIsActiveTrue(Integer userId, Integer productId);

    List<PriceAlert> findByIsActiveTrue();

    boolean existsByUserIdAndProductIdAndIsActiveTrue(Integer userId, Integer productId);

    long countByUserId(Integer userId);
}
