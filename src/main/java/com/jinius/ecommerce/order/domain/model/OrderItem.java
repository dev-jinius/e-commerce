package com.jinius.ecommerce.order.domain.model;

import lombok.*;

import java.math.BigInteger;

/**
 * 주문 상품 정보
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigInteger productPrice;
    private BigInteger totalPrice;
    private Long quantity;
    private OrderItemStatus status;

    public OrderItem(Long productId, BigInteger productPrice, Long quantity) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public BigInteger calculateTotalPrice() {
        return productPrice.multiply(BigInteger.valueOf(quantity));
    }

}