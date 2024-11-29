package com.jinius.ecommerce.payment.domain.model;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private PaymentType type;
    private BigInteger amount;
    private BigInteger point;
    private PaymentStatus status;
}
