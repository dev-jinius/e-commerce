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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jinius.ecommerce.order.domain.OrderStatus.*;

@Service
public class OrderService {
    private final StubUserService stubUserService;          //유저 서비스
    private final StubPaymentService stubPaymentService;    //결제 서비스
    private final StubProductService stubProductService;    //상품 서비스
    private final OrderRepository orderRepository;

    public OrderService(StubUserService stubUserService, StubPaymentService stubPaymentService, StubProductService stubProductService, OrderRepository orderRepository) {
        this.stubUserService = stubUserService;
        this.stubPaymentService = stubPaymentService;
        this.stubProductService = stubProductService;
        this.orderRepository = orderRepository;
    }

    /**
     * 주문 생성
     * @param request OrderRequest
     * @return Order
     */
    @Transactional
    public Order createOrder(OrderRequest request) {

        //유저 확인
        User user = stubUserService.validateUserByUserId(request.getUserId());

        //주문서 생성
        OrderSheet orderSheet = OrderSheet.from(request);
        orderSheet.log();
        Order order = orderRepository.create(orderSheet);

        try {
            //잔액(포인트) 확인
            stubUserService.comparePoint(user, orderSheet.getTotalPrice());

            //결제
            stubPaymentService.pay(user, order);
            order = updateOrderStatus(order, PAID);

            //재고 처리
            stubProductService.decreaseStock(orderSheet.getOrderItems());
            order = updateOrderStatus(order, COMPLETED);

            return order;
        } catch (EcommerceException e) {
            order = updateOrderStatus(order, CANCELED);
//            System.out.println("order.getOrderStatus() = " + order.getOrderStatus());
            throw e;
        }
    }

    /**
     * 주문 상태 값 변경
     * @param order
     * @param status
     * @return Order
     */
    public Order updateOrderStatus(Order order, OrderStatus status) {
        if (
            (status == PAID && order.getOrderStatus() != PENDING) ||
            (status == COMPLETED && order.getOrderStatus() != PAID)
        ) {
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);
        }

        order.setOrderStatus(status);
        orderRepository.updateStatus(order);
        return order;
    }
}
