package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.EcommerceApplication;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.order.infra.StubOrderRepository;
import com.jinius.ecommerce.payment.domain.StubPaymentService;
import com.jinius.ecommerce.product.domain.StubProductService;
import com.jinius.ecommerce.user.domain.StubUserService;
import com.jinius.ecommerce.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

//@Service
public class OrderService {
    private final StubUserService stubUserService;          //유저 서비스
    private final StubPaymentService stubPaymentService;    //결제 서비스
    private final StubProductService stubProductService;    //상품 서비스
    private final StubOrderRepository stubOrderRepository;

    public OrderService(StubUserService stubUserService, StubPaymentService stubPaymentService, StubProductService stubProductService, StubOrderRepository stubOrderRepository) {
        this.stubUserService = stubUserService;
        this.stubPaymentService = stubPaymentService;
        this.stubProductService = stubProductService;
        this.stubOrderRepository = stubOrderRepository;
    }

    @Transactional
    public Order createOrder(OrderRequest request) {

        //유저 확인
        User user = stubUserService.validateUserByUserId(request.getUserId());

        //주문서 생성
        OrderSheet orderSheet = OrderSheet.from(request);
        orderSheet.log();
        Order order = stubOrderRepository.create(orderSheet);

        try {
            //잔액(포인트) 확인
            stubUserService.comparePoint(user, orderSheet.getTotalPrice());

            //결제
            stubPaymentService.pay(user, order);
            order = updateOrderStatus(order, "PAID");

            //재고 처리
            stubProductService.decreaseStock(orderSheet.getOrderItems());
            order = updateOrderStatus(order, "COMPLETED");

            return order;
        } catch (EcommerceException e) {
            return updateOrderStatus(order, "CANCELED");
        }
    }

    public Order updateOrderStatus(Order order, String status) {
        if (
            (status.equals("PAID") && !order.getOrderStatus().equals("PENDING")) ||
            (status.equals("COMPLETED") && !order.getOrderStatus().equals("PAID"))
        ) {
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);
        }

        order.setOrderStatus(status);
        return stubOrderRepository.updateStatus(order);
    }
}
