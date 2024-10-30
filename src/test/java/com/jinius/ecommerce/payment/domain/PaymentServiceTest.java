package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderItem;
import com.jinius.ecommerce.order.domain.OrderStatus;
import com.jinius.ecommerce.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

class PaymentServiceTest {

    @Test
    @DisplayName("결제 요청 시 결제 금액이 0 이하인 경우 결제 실패")
    void createPay_negativeOrderPrice_failed() {
        //given
        User user = new User(1L, "이리스", BigInteger.valueOf(50000));
        Order order = new Order(
            1L,
            user.getUserId(),
            List.of(
                    new OrderItem(1L, BigInteger.valueOf(49900), 1L),
                    new OrderItem(3L, BigInteger.valueOf(30000), 2L)
            ),
            "POINT",
            BigInteger.ZERO,
            OrderStatus.PENDING,
            LocalDateTime.now()
        );

        //when
        Throwable exception = null;
        try {
//            sut.pay(user, order);
        } catch (EcommerceException e) {
            exception = e;
        }

        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
    }

}