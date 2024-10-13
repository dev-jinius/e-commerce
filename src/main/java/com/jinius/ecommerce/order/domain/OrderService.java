package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.user.domain.StubUserService;

//@Service
public class OrderService {

    private final StubUserService stubUserService;

    public OrderService(StubUserService stubUserService) {
        this.stubUserService = stubUserService;
    }

    //주문서 생성
    public void createOrder(OrderRequest request) {
        //유저 확인
        stubUserService.checkUser(request.getUserId());

        //주문서 생성
    }
}
