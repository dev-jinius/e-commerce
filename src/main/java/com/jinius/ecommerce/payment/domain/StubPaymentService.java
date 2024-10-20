package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.user.domain.User;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;

public class StubPaymentService {

    public void pay(User user, Order order) {
        if (ObjectUtils.isEmpty(user))
            throw new EcommerceException(ErrorCode.NOT_FOUND_USER);

        if (ObjectUtils.isEmpty(order))
            throw new EcommerceException(ErrorCode.NOT_FOUND_ORDER);

        if (order.getTotalPrice().compareTo(BigInteger.ZERO) <= 0)
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);

        if (!order.getOrderStatus().equals("PENDING"))
            throw new EcommerceException(ErrorCode.ALREADY_PAID_ORDER);

        //포인트 차감
        BigInteger balance = user.getPoint().subtract(order.getTotalPrice());
        user.setPoint(balance);

        //결제 정보 생성
        Payment.create(user, order);
    }
}
