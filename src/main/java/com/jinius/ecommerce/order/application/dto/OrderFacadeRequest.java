package com.jinius.ecommerce.order.application.dto;

import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import lombok.*;

import java.util.List;

import static com.jinius.ecommerce.order.domain.model.OrderStatus.PENDING;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderFacadeRequest {
    private Long userId;                                //유저 ID
    private List<OrderItemFacadeRequest> orderItems;    //주문 상품 목록

    //주문서 생성
    //현재 포인트 결제만 가능
    public OrderSheet createOrderSheet() {
        List<OrderItem> orderItems = this.orderItems.stream()
                .map(item -> new OrderItem(item.getProductId(), item.getPrice(), item.getQuantity()))
                .toList();

        return OrderSheet.builder()
                .userId(getUserId())
                .orderItems(orderItems)
                .totalPrice(OrderSheet.calculateOrderTotalPrice(orderItems))
                .orderStatus(PENDING)
                .build();
    }
}
