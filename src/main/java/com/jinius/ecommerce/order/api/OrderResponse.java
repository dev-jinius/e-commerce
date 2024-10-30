package com.jinius.ecommerce.order.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinius.ecommerce.order.domain.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;                           //주문 ID
    private String status;                          //주문 상태 [PENDING, PAID, COMPLETED, CANCELED, PARTIAL_REFUND]
    private BigInteger totalPrice;                  //총 주문 가격
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;                //주문 일시
    private List<OrderItemResponse> orderItems;     //주문 상품 리스트

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getOrderId(),
            order.getOrderStatus().name(),
            order.getTotalPrice(),
            order.getOrderDate(),
            order.getOrderItems().stream()
                    .map(OrderItemResponse::from)
                    .collect(Collectors.toList())
        );
    }
}
