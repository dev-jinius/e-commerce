package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderSheet;
import com.jinius.ecommerce.order.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @Column(name = "user_id")
    private Long userId;

    /**
     * 주문 상태 [PENDING, PAID, COMPLETED, CANCELED, PARTIAL_REFUND]
     *  PENDING - 대기중
     *  PAID - 결제 완료
     *  COMPLETED - 주문 완료
     *  CANCELED - 주문 취소
     *  PARTIAL_REFUND - 부분 환불/취소
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 총 주문 가격
     */
    @Column(name = "total_price", columnDefinition = "BIGINT")
    private BigInteger totalPrice;

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

    public static OrderEntity fromDomain(OrderSheet orderSheet) {
        return OrderEntity.builder()
                .userId(orderSheet.getUserId())
                .status(orderSheet.getOrderStatus())
                .totalPrice(orderSheet.getTotalPrice())
                .build();
    }

    public static OrderEntity fromDomain(Order order) {
        return OrderEntity.builder()
                .id(order.getOrderId())
                .userId(order.getUserId())
                .status(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .build();
    }


    public Order toDomain(OrderSheet orderSheet) {
        return Order.builder()
                .orderId(getId())
                .userId(getUserId())
                .orderItems(orderSheet.getOrderItems())
                .paymentType(orderSheet.getPaymentType())
                .totalPrice(getTotalPrice())
                .orderStatus(getStatus())
                .orderDate(getCreatedAt())
                .build();
    }

    public Order toDomain() {
        return Order.builder()
                .orderId(getId())
                .userId(getUserId())
                .totalPrice(getTotalPrice())
                .orderStatus(getStatus())
                .orderDate(getCreatedAt())
                .build();
    }
}