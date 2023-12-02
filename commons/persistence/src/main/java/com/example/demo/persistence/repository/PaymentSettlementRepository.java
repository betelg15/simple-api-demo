package com.example.demo.persistence.repository;

import com.example.demo.persistence.repository.entity.PaymentSettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSettlementRepository extends JpaRepository<PaymentSettlementEntity, Long> {
}
