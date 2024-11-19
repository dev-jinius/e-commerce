package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.payment.domain.model.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
