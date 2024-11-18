package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderStatus;
import com.jinius.ecommerce.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService sut;

    @Mock
    PaymentRepository paymentRepository;


    @Test
    @DisplayName("결제 요청 시 결제 금액이 0 이하인 경우 결제 실패")
    void createPay_negativeOrderPrice_failed() {
        //given
        User user = Fixture.user(1L, 50000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(-100000));

        //when
        Throwable exception = null;
        try {
            sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
    }

    @Test
    @DisplayName("결제 요청 시 유저가 NULL인 경우 결제 실패")
    void createPay_NullUser_failed() {
        //given
        User user = null;
        Order order = Fixture.order(1L);

        //when
        Throwable exception = null;
        try {
            sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
    }

    @Test
    @DisplayName("결제 요청 시 주문이 NULL인 경우 결제 실패")
    void createPay_NullOrder_failed() {
        //given
        User user = Fixture.user(1L, 50000);
        Order order = null;

        //when
        Throwable exception = null;
        try {
            sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
    }

    @Test
    @DisplayName("결제 요청 시 주문 상태 값이 'PENDING'이 아닌 경우 결제 실패")
    void createPay_notPendingStatus_failed() {
        //given
        User user = Fixture.user(1L, 50000);
        Order order = Fixture.order(1L);
        order.setOrderStatus(OrderStatus.COMPLETED);

        //when
        Throwable exception = null;
        try {
            sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.ALREADY_PAID_ORDER;
    }

    @Test
    @DisplayName("결제 요청 시 유저 잔액 포인트보다 주문 금액이 큰 경우 결제 실패")
    void createPay_notEnoughUserPoint_failed() {
        //given
        User user = Fixture.user(1L, 50000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(100000));

        //when
        Throwable exception = null;
        try {
            sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_ENOUGH_POINT;
    }

    @Test
    @DisplayName("포인트 결제 요청 시 결제 성공")
    void createPay_pointPay_success() {
        //given
        User user = Fixture.user(1L, 100000);
        Order order = Fixture.order(1L);
        order.setTotalPrice(BigInteger.valueOf(50000));

        //when
        when(paymentRepository.save(any(Payment.class))).thenReturn(Fixture.paymentOrder(user, order));
        Throwable exception = null;
        Payment payment = null;
        try {
            payment = sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception == null;
        assert payment.getType().equals("POINT");
        assert payment.getPoint() == order.getTotalPrice();
    }
}