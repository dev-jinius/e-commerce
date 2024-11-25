package com.jinius.ecommerce.payment.domain.model;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import lombok.*;

import java.math.BigInteger;

import static com.jinius.ecommerce.payment.domain.model.PaymentType.POINT;

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
