package com.jinius.ecommerce.order.application;

import com.jinius.ecommerce.order.application.dto.OrderDto;
import com.jinius.ecommerce.order.domain.*;
import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.payment.domain.PaymentService;
import com.jinius.ecommerce.payment.domain.model.Payment;
import com.jinius.ecommerce.payment.domain.model.PaymentStatus;
import com.jinius.ecommerce.payment.domain.model.PaymentType;
import com.jinius.ecommerce.product.domain.StockService;
import com.jinius.ecommerce.user.domain.UserPointService;
import com.jinius.ecommerce.user.domain.model.User;
import com.jinius.ecommerce.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jinius.ecommerce.order.domain.model.OrderStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;            //주문 서비스
    private final UserService userService;              //유저 서비스
    private final UserPointService userPointService;    //유저 포인트 서비스
    private final PaymentService paymentService;        //결제 서비스
    private final StockService stockService;            //재고 서비스

    /**
     * 주문 요청 처리
     * @param request
     * @return
     */
    @Transactional
    public OrderDto order(OrderDto request) {

        //유저 확인
        User user = userService.getUser(request.getUserId());
        //주문서 생성
        OrderSheet orderSheet = orderService.createOrderSheet(request.toOrderSheet());
        //주문 및 주문 상품 생성 //OrderItems 동시 재고 차감 시 데드락 방지를 위해 productId 순으로 정렬
        Order order = orderService.createOrder(orderSheet);

        //결제 정보 생성
        Payment payment = paymentService.createPayment(request.toOrderPayment(user, order, PaymentType.POINT));
        //결제 처리
        userPointService.usePoint(user, orderSheet.getTotalPrice());
        paymentService.updateStatus(payment, PaymentStatus.PAID);
        orderService.updateOrderStatus(order, PAID);
        //재고 처리 + 분산락 적용
        stockService.decreaseStock(order.getOrderItems());
        orderService.updateOrderStatus(order, COMPLETED);

        return OrderDto.from(order);

//        try {
//            //결제 정보 생성
//            Payment payment = paymentService.createPayment(request.toOrderPayment(user, order, PaymentType.POINT));
//            //결제 처리
//            userPointService.usePoint(user, orderSheet.getTotalPrice());
//            paymentService.updateStatus(payment, PaymentStatus.PAID);
//            orderService.updateOrderStatus(order, PAID);
//            //재고 처리 + 분산락 적용
//            stockService.decreaseStock(order.getOrderItems());
//            orderService.updateOrderStatus(order, COMPLETED);
//            return OrderDto.from(order);
//        } catch (EcommerceException | LockeException e) {
//            // 주문 취소 상태 업데이트
//            orderService.updateOrderStatus(order, CANCELED);
//            orderService.updateOrderItemStatus(order.getOrderItems(), OrderItemStatus.CANCELED);
//            return OrderDto.from(order);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw e;
//        }
    }
}
