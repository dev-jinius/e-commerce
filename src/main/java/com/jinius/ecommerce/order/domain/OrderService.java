package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jinius.ecommerce.order.domain.model.OrderItemStatus.DELIVERED;
import static com.jinius.ecommerce.order.domain.model.OrderStatus.*;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문 생성
     * @param orderSheet
     * @return Order
     */
    @Transactional
    public Order createOrder(OrderSheet orderSheet) {
        //주문서 출력
        orderSheet.log();
        //주문 생성
        Order order = orderRepository.create(orderSheet);
        //주문 상품 생성
        orderItemRepository.create(order);
        return order;
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
