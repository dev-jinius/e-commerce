package com.jinius.ecommerce.payment.domain.model;

import lombok.*;

import java.math.BigInteger;

/**
 * 주문 결제 정보
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderPaymentInfo {
    private Long orderId;
    private Long userId;
    private PaymentType type;
    private BigInteger amount;
    private BigInteger point;
    private PaymentStatus status;
}
