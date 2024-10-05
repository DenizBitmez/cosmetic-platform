package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
