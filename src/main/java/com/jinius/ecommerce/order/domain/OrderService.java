package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.payment.domain.StubPaymentService;
import com.jinius.ecommerce.user.domain.StubUserService;
import com.jinius.ecommerce.user.domain.User;

//@Service
public class OrderService {

    private final StubUserService stubUserService;
    private final StubPaymentService stubPaymentService;

    public OrderService(StubUserService stubUserService, StubPaymentService stubPaymentService) {
        this.stubUserService = stubUserService;
        this.stubPaymentService = stubPaymentService;
    }

    public OrderSheet createOrder(OrderRequest request) {
        //유저 확인
        User user = stubUserService.validateUserByUserId(request.getUserId());

        //주문서 생성
        OrderSheet orderSheet = OrderSheet.from(request);
        orderSheet.log();

        //잔액(포인트) 확인
        stubUserService.comparePoint(user, orderSheet.getTotalPrice());

        //결제

        //재고 처리

        return orderSheet;
    }
}
