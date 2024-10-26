package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment {
    private Long orderId;
    private Long userId;
    private String type;
    private BigInteger amount;
    private BigInteger point;
    private String status;
    private LocalDateTime paidAt;

    public static Payment create(User user, Order order) {
        return new Payment(
                order.getOrderId(),
                user.getUserId(),
                "POINT",
                BigInteger.ZERO,
                order.getTotalPrice(),
                "PENDING",
                LocalDateTime.now()
        );
    }
}
