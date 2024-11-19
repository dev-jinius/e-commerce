package com.jinius.ecommerce.payment.domain.model;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import lombok.*;

import java.math.BigInteger;

/**
 * 주문 결제 정보
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderPayment {
    private Long orderId;
    private Long userId;
    private BigInteger userPoint;
    private PaymentType type;
    private BigInteger orderPrice;
}

