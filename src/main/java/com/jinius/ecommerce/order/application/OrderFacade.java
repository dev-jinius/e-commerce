package com.jinius.ecommerce.order.application;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.order.application.dto.OrderFacadeRequest;
import com.jinius.ecommerce.order.application.dto.OrderFacadeResponse;
import com.jinius.ecommerce.order.domain.*;
import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderItemStatus;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.payment.domain.PaymentService;
import com.jinius.ecommerce.product.domain.ProductService;
import com.jinius.ecommerce.user.domain.User;
import com.jinius.ecommerce.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jinius.ecommerce.order.domain.model.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;        //주문 서비스
    private final UserService userService;          //유저 서비스
    private final PaymentService paymentService;    //결제 서비스
    private final ProductService productService;    //상품 서비스

    /**
     * 주문 요청 처리
     * @param request
     * @return
     */
    @Transactional
    public OrderFacadeResponse order(OrderFacadeRequest request) {

        //유저 확인
        User user = userService.validateUserByUserId(request.getUserId());
        //주문서 생성
        OrderSheet orderSheet = orderService.createOrderSheet(request.toOrderSheet());
        //주문 생성
        Order order = orderService.createOrder(orderSheet);

        try {
            //결제
            paymentService.pay(user, order);
            orderService.updateOrderStatus(order, PAID);
            //재고 처리
            productService.decreaseStock(order.getOrderItems());
            orderService.updateOrderStatus(order, COMPLETED);

            return OrderFacadeResponse.from(order);
        } catch (EcommerceException e) {
            // 주문 취소 상태 업데이트
            orderService.updateOrderStatus(order, CANCELED);
            orderService.updateOrderItemStatus(order.getOrderItems(), OrderItemStatus.CANCELED);
            throw e;
        }
    }
}
