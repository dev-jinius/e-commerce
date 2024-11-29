package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.order.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Comparator;
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
     * 주문서 생성
     * @return
     */
    public OrderSheet createOrderSheet(OrderSheet orderSheet) {
        //총 주문 금액 계산
        orderSheet.calculateOrderTotalPrice(orderSheet.getOrderItems());

        //총 주문금액이 0원 이하인 경우
        if (orderSheet.getTotalPrice().compareTo(BigInteger.ZERO) <= 0) throw new EcommerceException(ErrorCode.INVALID_TOTAL_PRICE);

        //주문서 출력
        orderSheet.log();
        return orderSheet;
    }
    
    /**
     * 주문 생성
     * @param orderSheet
     * @return Order
     */
    @Transactional
    public Order createOrder(OrderSheet orderSheet) {
        if (orderSheet == null) throw new EcommerceException(ErrorCode.NOT_FOUND_ORDER_SHEET);

        Order order = orderRepository.create(orderSheet);
        if (order == null) throw new EcommerceException(ErrorCode.FAIL_CREATE_ORDER);

        createOrderItems(order);
        return order;
    }

    /**
     * 주문 상품 생성
     * @param order
     * @return
     */
    @Transactional
    public void createOrderItems(Order order) {
        List<OrderItem> orderItems = orderItemRepository.create(order);

        if (orderItems.size() != order.getOrderItems().size())
            throw new EcommerceException(ErrorCode.FAIL_CREATE_ORDER_ITEMS);
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
            throw new EcommerceException(ErrorCode.INVALID_ORDER_STATUS);
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
     * 60초마다 주문 완료된 주문 상품 배송 처리
     */
    @Scheduled(fixedRate = 60000)
    public void updateOrderItemStatusToDELIVERED() {
        List<Long> preparingItems = orderItemRepository.findPreparingItems();
        if (preparingItems.size() == 0) return;

        orderItemRepository.updateStatus(preparingItems, DELIVERED);
    }
}
