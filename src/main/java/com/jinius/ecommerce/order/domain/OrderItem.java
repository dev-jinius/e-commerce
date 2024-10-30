package com.jinius.ecommerce.order.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * 주문 상품 정보
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {
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