package com.jinius.ecommerce.order.domain;

public interface OrderRepository {
    Order create(OrderSheet orderSheet);

    Order updateStatus(Order order);
}
