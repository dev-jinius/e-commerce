package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.payment.domain.model.OrderPaymentInfo;
import com.jinius.ecommerce.payment.domain.model.Payment;

public interface PaymentRepository {

    Payment save(OrderPaymentInfo orderPaymentInfo);

    void updateStatus(Payment payment);
}
