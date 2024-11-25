package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.order.domain.model.OrderItemStatus;

import java.util.List;

public interface OrderItemRepository {

    List<OrderItem> create(Order order);

    void updateStatus(List<Long> itemList, OrderItemStatus status);

    //PREPARE 상태의 주문 상품 조회
    List<Long> findPreparingItems();
}
