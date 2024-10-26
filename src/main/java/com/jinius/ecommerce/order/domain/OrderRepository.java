package com.jinius.ecommerce.order.domain;

public interface OrderRepository {
    
    //주문서 생성
    Order create(OrderSheet orderSheet);

    //주문 상태 업데이트
    void updateStatus(Order order);
}
