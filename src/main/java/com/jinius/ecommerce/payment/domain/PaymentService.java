package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.payment.domain.model.OrderPayment;
import com.jinius.ecommerce.payment.domain.model.OrderPaymentInfo;
import com.jinius.ecommerce.payment.domain.model.Payment;
import com.jinius.ecommerce.payment.domain.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.jinius.ecommerce.payment.domain.model.PaymentStatus.PAID;
import static com.jinius.ecommerce.payment.domain.model.PaymentStatus.PNEDING;

/**
 * 결제 서비스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 주문 결제 정보 생성
     * @param orderPayment
     * @return
     */
    @Transactional
    public Payment createPayment(OrderPayment orderPayment) {
        OrderPaymentInfo orderPaymentInfo = orderPayment.create();
        return paymentRepository.save(orderPaymentInfo);
    }

    /**
     * 결제 상태 업데이트
     * @param payment
     */
    @Transactional
    public void updateStatus(Payment payment, PaymentStatus status) {
        //이미 결제한 주문인지 확인
        if (status == PAID && payment.getStatus() != PNEDING)
            throw new EcommerceException(ErrorCode.ALREADY_PAID_ORDER);

        payment.setStatus(PAID);
        paymentRepository.updateStatus(payment);
    }
}
