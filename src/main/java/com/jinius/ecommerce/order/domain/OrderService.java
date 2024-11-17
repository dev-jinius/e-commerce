package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.payment.domain.StubPaymentService;
import com.jinius.ecommerce.product.domain.StubProductService;
import com.jinius.ecommerce.user.domain.StubUserService;
import com.jinius.ecommerce.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jinius.ecommerce.order.domain.OrderItemStatus.DELIVERED;
import static com.jinius.ecommerce.order.domain.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final StubUserService userService;          //유저 서비스
    private final StubPaymentService paymentService;    //결제 서비스
    private final StubProductService productService;    //상품 서비스

    @Qualifier(value = "orderRepositoryImpl")
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문 생성
     * @param request OrderRequest
     * @return Order
     */
    @Transactional
    public Order createOrder(OrderRequest request) {

        //유저 확인
        User user = userService.validateUserByUserId(request.getUserId());

        //주문서 생성
        OrderSheet orderSheet = OrderSheet.from(request);
        orderSheet.log();
        Order order = orderRepository.create(orderSheet);
        List<OrderItem> orderItems = orderItemRepository.create(order);

        try {
            //잔액(포인트) 확인
            userService.comparePoint(user, orderSheet.getTotalPrice());

            //결제
            paymentService.pay(user, order);
            updateOrderStatus(order, PAID);

            //재고 처리
            productService.decreaseStock(orderSheet.getOrderItems());
            updateOrderStatus(order, COMPLETED);
            
            return order;
        } catch (EcommerceException e) {
            updateOrderStatus(order, CANCELED);
            updateOrderItemStatus(orderItems, OrderItemStatus.CANCELED);
            throw e;
        }
    }

    /**
     * 주문 상태 값 변경
     * @param order
     * @param status
     * @return Order
     */
    @Transactional
    public void updateOrderStatus(Order order, OrderStatus status) {
        if (
            (status == PAID && order.getOrderStatus() != PENDING) ||
            (status == COMPLETED && order.getOrderStatus() != PAID)
        ) {
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);
        }

        orderRepository.updateStatus(order.getOrderId(), status);
        order.setOrderStatus(status);
    }

    /**
     * 주문 상품 상태 값 변경
     * @param orderItems
     * @param status
     */
    @Transactional
    public void updateOrderItemStatus(List<OrderItem> orderItems, OrderItemStatus status) {
        orderItemRepository.updateStatus(orderItems.stream().map(item -> item.getId()).collect(Collectors.toList()), status);
    }

    /**
     * 10초마다 주문 완료된 주문 상품 배송 처리
     */
    @Scheduled(fixedRate = 10000) 
    public void updateOrderItemStatusToDELIVERED() {
        List<Long> preparingItems = orderItemRepository.findPreparingItems();

        orderItemRepository.updateStatus(preparingItems, DELIVERED);
    }
}
