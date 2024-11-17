package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.OrderItem;
import com.jinius.ecommerce.order.domain.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
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
     * 상품 상태 [PREPARING, SHIPPING, DELIVERED, CANCELED]
     *  PREPARING - 배송 준비 중
     *  SHIPPING  - 배송 중
     *  DELIVERED - 배송 완료
     *  CANCELED  - 취소
     */
    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    /**
     * 주문 시간
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 상태 업데이트 시간
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static OrderItemEntity from(Long orderId, OrderItem orderItem, OrderItemStatus status) {
        return OrderItemEntity.builder()
                .orderId(orderId)
                .productId(orderItem.getProductId())
                .price(orderItem.getProductPrice())
                .quantity(orderItem.getQuantity())
                .status(status)
                .build();
    }

    public OrderItem toOrderItem() {
        return OrderItem.builder()
                .id(getId())
                .orderId(getOrderId())
                .productId(getProductId())
                .productPrice(getPrice())
                .quantity(getQuantity())
                .status(getStatus())
                .build();
    }
}
