package com.jinius.ecommerce.order.application.dto;

import com.jinius.ecommerce.order.domain.model.Order;
import lombok.*;
import com.jinius.ecommerce.order.api.dto.OrderItemResponse;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderFacadeResponse {

    private Long orderId;                           //주문 ID
    private String status;                          //주문 상태 [PENDING, PAID, COMPLETED, CANCELED, PARTIAL_REFUND]
    private BigInteger totalPrice;                  //총 주문 가격
    private LocalDateTime orderedAt;                //주문 일시
    private List<OrderItemFacadeResponse> orderItems;     //주문 상품 리스트

    public static OrderFacadeResponse from(Order order) {
        return OrderFacadeResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getOrderStatus().name())
                .totalPrice(order.getTotalPrice())
                .orderedAt(order.getOrderDate())
                .orderItems(order.getOrderItems().stream()
                        .map(OrderItemFacadeResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
