package com.jinius.ecommerce.order.domain;

import java.util.List;

public interface OrderItemRepository {

    List<OrderItem> create(Order order);

    void updateStatus(List<Long> itemList, OrderItemStatus status);

    //PREPARE 상태의 주문 상품 조회
    List<Long> findPreparingItems();
}
