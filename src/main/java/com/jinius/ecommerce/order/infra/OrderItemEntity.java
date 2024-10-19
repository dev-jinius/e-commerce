package com.jinius.ecommerce.order.infra;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_order_item")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
public class OrderItemEntity {
    /**
     * 주문 상품 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    /**
     * 주문 정보
     */
    private Long orderId;


    /**
     * 상품 정보
     */
    private Long productId;

    /**
     * 상품 가격
     */
    @Column(name = "product_price", nullable = false, columnDefinition = "BIGINT")
    private BigInteger price;

    /**
     * 주문 상품 수량
     */
    @Column(nullable = false)
    private Long quantity;

    /**
     * 상품 상태 [PREPARING, SHIPPING, DELIVERED, PARTIAL_CANCELED]
     *  PREPARING - 배송 준비 중
     *  SHIPPING - 배송 중
     *  DELIVERED - 배송 완료
     *  PARTIAL_CANCELED - 부분 취소
     */
    private String status;

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
