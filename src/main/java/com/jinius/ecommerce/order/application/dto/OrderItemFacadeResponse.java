package com.jinius.ecommerce.order.application.dto;

import com.jinius.ecommerce.order.domain.model.OrderItem;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemFacadeResponse {
    private Long productId;     //상품 ID
    private BigInteger price;   //상품 가격
    private Long quantity;      //상품 수량

    public static OrderItemFacadeResponse from(OrderItem orderItem) {
        return OrderItemFacadeResponse.builder()
                .productId(orderItem.getProductId())
                .price(orderItem.getProductPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
