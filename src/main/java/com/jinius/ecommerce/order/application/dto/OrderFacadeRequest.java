package com.jinius.ecommerce.order.application.dto;

import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.payment.domain.model.OrderPayment;
import com.jinius.ecommerce.payment.domain.model.Payment;
import com.jinius.ecommerce.payment.domain.model.PaymentStatus;
import com.jinius.ecommerce.payment.domain.model.PaymentType;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

import static com.jinius.ecommerce.order.domain.model.OrderStatus.PENDING;
import static com.jinius.ecommerce.payment.domain.model.PaymentStatus.PAID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderFacadeRequest {
    private Long userId;                                //유저 ID
    private List<OrderItemFacadeRequest> orderItems;    //주문 상품 목록

    public OrderSheet toOrderSheet() {
        List<OrderItem> orderItems = this.orderItems.stream()
                .map(item -> new OrderItem(item.getProductId(), item.getPrice(), item.getQuantity()))
                .toList();

        return OrderSheet.builder()
                .userId(this.userId)
                .orderItems(orderItems)
                .build();
    }

    public OrderPayment toOrderPayment(User user, Order order, PaymentType paymentType) {
        return OrderPayment.builder()
                .userId(order.getUserId())
                .orderId(order.getOrderId())
                .type(paymentType)
                .orderPrice(order.getTotalPrice())
                .build();
    }
}
