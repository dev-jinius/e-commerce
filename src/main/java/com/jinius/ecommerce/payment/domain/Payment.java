package com.jinius.ecommerce.payment.domain;

import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.user.domain.User;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {
    private Long orderId;
    private Long userId;
    private String type;
    private BigInteger amount;
    private BigInteger point;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static Payment create(User user, Order order) {
        return new Payment(
                order.getOrderId(),
                user.getUserId(),
                "POINT",
                BigInteger.ZERO,
                order.getTotalPrice(),
                "PENDING",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
