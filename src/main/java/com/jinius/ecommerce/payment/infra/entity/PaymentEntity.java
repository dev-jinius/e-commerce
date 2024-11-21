package com.jinius.ecommerce.payment.infra.entity;

import com.jinius.ecommerce.payment.domain.model.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_payment")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type")
    private PaymentType type;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "point")
    private BigInteger point;

    /**
     * 결제 상태 [PENDING, PAID, FAILED, REFUNDED, PARTIAL_REFUND]
     *  PENDING - 결제 대기
     *  PAID - 결제 완료
     *  FAILED - 결제 실패
     *  REFUNDED - 전체 환불
     *  PARTIAL_REFUND - 부분 환불
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static PaymentEntity fromPayment(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .type(payment.getType())
                .amount(payment.getAmount())
                .point(payment.getPoint())
                .status(payment.getStatus())
                .build();
    }

    public static PaymentEntity fromOrderPaymentInfo(OrderPaymentInfo orderPaymentInfo) {
        return PaymentEntity.builder()
                .orderId(orderPaymentInfo.getOrderId())
                .userId(orderPaymentInfo.getUserId())
                .type(orderPaymentInfo.getType())
                .amount(orderPaymentInfo.getAmount())
                .point(orderPaymentInfo.getPoint())
                .status(orderPaymentInfo.getStatus())
                .build();
    }

    public Payment toDomain() {
        return Payment.builder()
                .paymentId(getId())
                .orderId(getOrderId())
                .userId(getUserId())
                .type(getType())
                .amount(getAmount())
                .point(getPoint())
                .status(getStatus())
                .build();
    }
}
