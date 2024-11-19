package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.payment.domain.model.OrderPayment;
import com.jinius.ecommerce.payment.domain.model.Payment;
import com.jinius.ecommerce.payment.domain.model.PaymentStatus;
import com.jinius.ecommerce.payment.domain.model.PaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * 결제 서비스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 결제 정보 생성
     * @param orderPayment
     * @return
     */
    public Payment createPayment(OrderPayment orderPayment) {
        return paymentRepository.save(Payment.create(orderPayment));
    }

    /**
     * 결제 처리
     * @param payment
     */
    public void pay(Payment payment) {
        //이미 결제한 주문인지 확인
        if (payment.getStatus() != PaymentStatus.PNEDING)
            throw new EcommerceException(ErrorCode.ALREADY_PAID_ORDER);

        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
    }
}
