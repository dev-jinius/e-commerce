package com.jinius.ecommerce.payment.infra;

import com.jinius.ecommerce.payment.domain.Payment;
import com.jinius.ecommerce.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentResporitoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(PaymentEntity.fromDomain(payment)).toDomain();
    }
}