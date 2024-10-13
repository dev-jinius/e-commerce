package com.jinius.ecommerce.order.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderRequest {
    private Long userId;                        //유저 ID
    private List<OrderItemRequest> orderItems;  //주문 상품 목록
}
