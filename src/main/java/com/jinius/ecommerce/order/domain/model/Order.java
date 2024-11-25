package com.jinius.ecommerce.order.domain.model;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 (주문이 완료된 후 저장된 정보)
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {
    private Long orderId;
    private Long userId;
    private List<OrderItem> orderItems;
    private String paymentType;
    private BigInteger totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
}
