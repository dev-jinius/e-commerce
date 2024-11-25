package com.jinius.ecommerce.payment.infra;

import com.jinius.ecommerce.payment.domain.model.PaymentStatus;
import com.jinius.ecommerce.payment.infra.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE PaymentEntity " +
            "SET status = :status " +
            "WHERE id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") PaymentStatus status);
}
