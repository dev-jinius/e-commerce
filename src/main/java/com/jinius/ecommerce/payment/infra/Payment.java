package com.jinius.ecommerce.payment.infra;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_payment")
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Long orderId;

    private Long userId;

    private String type;

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
    private String status;

    @CreatedDate
    private LocalDateTime createAt;
}
