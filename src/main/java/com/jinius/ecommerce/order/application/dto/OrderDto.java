package com.jinius.ecommerce.order.application.dto;

import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.payment.domain.model.OrderPayment;
import com.jinius.ecommerce.payment.domain.model.PaymentType;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long userId;                                //유저 ID
    private List<OrderItemDto> orderItems;              //주문 상품 목록
    private Long orderId;                               //주문 ID
    private BigInteger totalPrice;                      //총 주문 가격
    private LocalDateTime orderedAt;                    //주문 일시

    public OrderSheet toOrderSheet() {
        List<OrderItem> orderItems = this.orderItems.stream()
                .map(item -> new OrderItem(item.getProductId(), item.getPrice(), item.getQuantity()))
                .sorted(Comparator.comparing(OrderItem::getProductId))
                .collect(Collectors.toList());

        return OrderSheet.builder()
                .userId(this.userId)
                .orderItems(orderItems)
                .build();
    }

    public OrderPayment toOrderPayment(User user, Order order, PaymentType paymentType) {
        return OrderPayment.builder()
                .userId(order.getUserId())
                .orderId(order.getOrderId())
                .userPoint(user.getPoint())
                .type(paymentType)
                .orderPrice(order.getTotalPrice())
                .build();
    }

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .totalPrice(order.getTotalPrice())
                .orderedAt(order.getOrderDate())
                .orderItems(order.getOrderItems().stream()
                        .map(OrderItemDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
