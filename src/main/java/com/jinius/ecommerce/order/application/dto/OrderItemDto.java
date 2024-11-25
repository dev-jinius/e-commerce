package com.jinius.ecommerce.order.application.dto;

import com.jinius.ecommerce.order.domain.model.OrderItem;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long productId;         //상품 ID
    private BigInteger price;       //상품 가격
    private Long quantity;          //상품 수량

    public static OrderItemDto from(OrderItem orderItem) {
        return OrderItemDto.builder()
                .productId(orderItem.getProductId())
                .price(orderItem.getProductPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
