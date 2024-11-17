package com.jinius.ecommerce.order.application.dto;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemFacadeRequest {
    private Long productId;         //상품 ID
    private BigInteger price;       //상품 가격
    private Long quantity;          //상품 수량
}
