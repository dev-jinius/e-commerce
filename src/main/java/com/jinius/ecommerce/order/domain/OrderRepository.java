package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.order.domain.model.OrderStatus;

public interface OrderRepository {
    
    //주문서 생성
    Order create(OrderSheet orderSheet);

    //주문 상태 업데이트
    void updateStatus(Long orderId, OrderStatus status);
}
