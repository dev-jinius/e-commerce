package com.jinius.ecommerce.order.infra;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
public class OrderEntity {

    /**
     * 주문 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    /**
     * 유저 정보
     */
    private Long userId;

    /**
     * 주문 상태 [PENDING, PAID, COMPLETED, CANCELED, PARTIAL_REFUND]
     *  PENDING - 대기중
     *  PAID - 결제 완료
     *  COMPLETED - 주문 완료
     *  CANCELED - 주문 취소
     *  PARTIAL_REFUND - 부분 환불/취소
     */
    private String status;

    /**
     * 총 주문 가격
     */
    @Column(columnDefinition = "BIGINT")
    private BigInteger totalPrice;

    /**
     * 주문 시간
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 상태 업데이트 시간
     */
    private LocalDateTime updatedAt;
}