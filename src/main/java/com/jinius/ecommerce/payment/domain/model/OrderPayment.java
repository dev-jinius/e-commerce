package com.jinius.ecommerce.payment.domain.model;

import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import lombok.*;

import java.math.BigInteger;

/**
 * 주문 결제
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderPayment {
    private Long orderId;
    private Long userId;
    private PaymentType type;
    private BigInteger userPoint;
    private BigInteger orderPrice;

    /**
     * 주문 결제 정보 생성
     * @return
     */
    public OrderPaymentInfo create() {
        //포인트 부족으로 결제 정보 생성 실패
        if (this.type == PaymentType.POINT
                && this.userPoint.compareTo(this.orderPrice) <= 0) {
            throw new EcommerceException(ErrorCode.NOT_ENOUGH_POINT);
        }
        //주문 금액 0원 이하로 결제 정보 생성 실패
        if (this.orderPrice.compareTo(BigInteger.ZERO) <= 0)
            throw new EcommerceException(ErrorCode.INVALID_TOTAL_PRICE);

        return OrderPaymentInfo.builder()
                .orderId(this.orderId)
                .userId(this.userId)
                .type(this.type)
                .amount(this.type == PaymentType.POINT ? BigInteger.ZERO : this.orderPrice)
                .point(this.type == PaymentType.POINT ? this.orderPrice : BigInteger.ZERO)
                .status(PaymentStatus.PNEDING)
                .build();
    }
}

