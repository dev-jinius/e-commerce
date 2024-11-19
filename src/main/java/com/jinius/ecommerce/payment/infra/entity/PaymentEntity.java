package com.jinius.ecommerce.payment.infra.entity;

import com.jinius.ecommerce.payment.domain.model.Payment;
import com.jinius.ecommerce.payment.domain.model.PaymentStatus;
import com.jinius.ecommerce.payment.domain.model.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
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

    private Long orderId;

    private Long userId;

    private PaymentType type;

    private BigInteger amount;

    private BigInteger point;

    /**
     * 결제 상태 [PENDING, PAID, FAILED, REFUNDED, PARTIAL_REFUND]
     *  PENDING - 결제 대기
     *  PAID - 결제 완료
     *  FAILED - 결제 실패
     *  REFUNDED - 전체 환불
     *  PARTIAL_REFUND - 부분 환불
     */
    private PaymentStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    public static PaymentEntity fromDomain(Payment payment) {
        return PaymentEntity.builder()
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .type(payment.getType())
                .amount(payment.getAmount())
                .point(payment.getPoint())
                .status(payment.getStatus())
                .build();
    }

    public Payment toDomain() {
        return Payment.builder()
                .orderId(this.orderId)
                .userId(this.userId)
                .type(this.type)
                .amount(this.amount)
                .point(this.point)
                .status(this.status)
                .build();
    }
}