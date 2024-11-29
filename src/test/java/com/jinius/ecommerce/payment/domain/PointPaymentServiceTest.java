package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.payment.domain.model.*;
import com.jinius.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static com.jinius.ecommerce.payment.domain.model.PaymentStatus.PAID;
import static com.jinius.ecommerce.payment.domain.model.PaymentStatus.PNEDING;
import static com.jinius.ecommerce.payment.domain.model.PaymentType.POINT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointPaymentServiceTest {

    @InjectMocks
    PaymentService sut;

    @Mock
    PaymentRepository paymentRepository;

    @Test
    @DisplayName("주문 결제 정보 생성 시 유저 잔액 포인트보다 주문 금액이 큰 경우 결제 실패")
    void createPay_notEnoughUserPoint_failed() {
        //given
        User user = Fixture.user(1L, 50000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(100000));
        OrderPayment orderPayment = Fixture.orderPayment(user, order, POINT);

        //when
        Throwable exception = null;
        try {
            sut.createPayment(orderPayment);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_ENOUGH_POINT;
    }

    @Test
    @DisplayName("주문 결제 정보 생성 시 주문 금액이 0 이하인 경우 결제 실패")
    void createPay_negativeOrderPrice_failed() {
        //given
        User user = Fixture.user(1L, 50000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(-100000));
        OrderPayment orderPayment = Fixture.orderPayment(user, order, POINT);

        //when
        Throwable exception = null;
        try {
            sut.createPayment(orderPayment);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_TOTAL_PRICE;
    }

    @Test
    @DisplayName("이미 결제한 주문 결제 요청 시 ALREADY_PAID_ORDER 예외 발생으로 결제 실패")
    void pay_fail_ALREADY_PAID_ORDER() {
        //given
        User user = Fixture.user(1L, 100000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(50000));
        Payment payment = Fixture.payment(Fixture.orderPaymentInfoForPoint(user, order, POINT));
        payment.setStatus(PAID);

        //when
        Throwable exception = null;
        try {
            sut.updateStatus(payment, PAID);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.ALREADY_PAID_ORDER;
    }

    @Test
    @DisplayName("결제 요청 시 결제 성공")
    void pay_success() {
        //given
        User user = Fixture.user(1L, 100000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(50000));
        Payment payment = Fixture.payment(Fixture.orderPaymentInfoForPoint(user, order, POINT));
        payment.setStatus(PNEDING);

        //when
        sut.updateStatus(payment, PAID);

        assert payment.getType().equals(POINT);
        verify(paymentRepository, atLeastOnce()).updateStatus(any());
    }
}