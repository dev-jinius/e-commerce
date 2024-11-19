package com.jinius.ecommerce.payment.infra;

import com.jinius.ecommerce.payment.infra.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
