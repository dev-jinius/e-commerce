package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderRepository;
import com.jinius.ecommerce.order.domain.OrderSheet;

import java.time.LocalDateTime;

public class StubOrderRepository implements OrderRepository {

    private long idCounter = 1;

    @Override
    public Order create(OrderSheet orderSheet) {
        return new Order(
            idCounter++,
            orderSheet.getUserId(),
            orderSheet.getOrderItems(),
            orderSheet.getPaymentType(),
            orderSheet.getTotalPrice(),
            orderSheet.getOrderStatus(),
            LocalDateTime.now()
        );
    }

    @Override
    public Order updateStatus(Order order) {
        return order;
    }
}
