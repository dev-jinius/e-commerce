package com.jinius.ecommerce.order.api;

import com.jinius.ecommerce.order.domain.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemResponse {
    private Long productId;     //상품 ID
    private BigInteger price;   //상품 가격
    private Long quantity;      //상품 수량

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProductId(),
                orderItem.getProductPrice(),
                orderItem.getQuantity()
        );
    }
}
