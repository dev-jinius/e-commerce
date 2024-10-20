package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.order.infra.StubOrderRepository;
import com.jinius.ecommerce.payment.domain.StubPaymentService;
import com.jinius.ecommerce.user.domain.StubUserService;
import com.jinius.ecommerce.user.domain.User;

//@Service
public class OrderService {

    private final StubUserService stubUserService;
    private final StubPaymentService stubPaymentService;
    private final StubOrderRepository stubOrderRepository;

    public OrderService(StubUserService stubUserService, StubPaymentService stubPaymentService, StubOrderRepository stubOrderRepository) {
        this.stubUserService = stubUserService;
        this.stubPaymentService = stubPaymentService;
        this.stubOrderRepository = stubOrderRepository;
    }

    public OrderSheet createOrder(OrderRequest request) {
        //유저 확인
        User user = stubUserService.validateUserByUserId(request.getUserId());

        //주문서 생성
        OrderSheet orderSheet = OrderSheet.from(request);
        orderSheet.log();
        Order order = stubOrderRepository.create(orderSheet);

        //잔액(포인트) 확인
        stubUserService.comparePoint(user, orderSheet.getTotalPrice());

        //결제
        stubPaymentService.pay(user, order);
        order = updateOrderStatus(order, "PAID");

        //재고 처리


        return orderSheet;
    }

    public Order updateOrderStatus(Order order, String status) {
        order.setOrderStatus(status);
        return stubOrderRepository.updateStatus(order);
    }
}
