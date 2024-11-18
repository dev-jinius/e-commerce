package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static com.jinius.ecommerce.order.domain.model.OrderStatus.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService sut;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문서 생성 시 총 주문 금액이 0원 이하인 경우 INVALID_TOTAL_PRICE 예외 발생")
    void createOrderSheet_fail_INVALID_TOTAL_PRICE() {
        //given
        OrderSheet orderSheet = Fixture.customOrderSheet(
                1L,
                    List.of(OrderItem.builder()
                            .productId(1L)
                            .productPrice(BigInteger.valueOf(-49900))
                            .quantity(2L)
                            .build())
                );

        //when
        Throwable exception = null;
        try {
            sut.createOrderSheet(orderSheet);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_TOTAL_PRICE;
    }

    @Test
    @DisplayName("주문서 생성 성공")
    void createOrderSheet_suceess() {
        //given
        OrderSheet orderSheet = Fixture.orderSheet(1L);

        //when
        OrderSheet result = sut.createOrderSheet(orderSheet);

        //then
        assert result != null;
        assert result.getUserId() == orderSheet.getUserId();
        assert result.getOrderItems().size() == orderSheet.getOrderItems().size();
        assert result.getTotalPrice().compareTo(orderSheet.calculateOrderTotalPrice(orderSheet.getOrderItems())) == 0;
        assert result.getTotalPrice().compareTo(BigInteger.ZERO) > 0;
    }

    @Test
    @DisplayName("주문 생성 시 주문서가 NULL 인 경우 NOT_FOUND_ORDER_SHEET 예외 발생")
    void createOrder_fail_NOT_FOUND_ORDER_SHEET() {
        //given
        OrderSheet orderSheet = null;

        //when
        Throwable exception = null;
        try {
            sut.createOrder(orderSheet);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_FOUND_ORDER_SHEET;
    }

    @Test
    @DisplayName("주문 생성에 실패하는 경우 FAIL_CREATE_ORDER 예외 발생")
    void createOrder_fail_FAIL_CREATE_ORDER() {
        //given
        OrderSheet orderSheet = Fixture.randomOrderSheet();

        //when
        when(orderRepository.create(any())).thenReturn(null);
        Throwable exception = null;
        try {
            sut.createOrder(orderSheet);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.FAIL_CREATE_ORDER;
    }

    @Test
    @DisplayName("주문 상품 생성 시 주문 상품 개수와 저장 후 상품 개수가 다른 경우 FAIL_CREATE_ORDER_ITEMS 예외 발생")
    void createOrder_fail_FAIL_CREATE_ORDER_ITEMS() {
        //given
        Order order = Fixture.order(1L);

        //when
        when(orderItemRepository.create(any())).thenReturn(Fixture.orderItems(0));
        Throwable exception = null;
        try {
            sut.createOrderItems(order);
        } catch (EcommerceException e) {
            exception = e;
        }
        System.out.println("exception = " + exception.getMessage());

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.FAIL_CREATE_ORDER_ITEMS;
    }

    @Test
    @DisplayName("주문 및 주문 상품 생성에 성공")
    void createOrder_success() {
        //given
        OrderSheet orderSheet = Fixture.orderSheet(1L);
        System.out.println("orderSheet.getUserId() = " + orderSheet.getUserId());
        System.out.println("orderSheet.getTotalPrice() = " + orderSheet.getTotalPrice());

        //when
        when(orderRepository.create(any())).thenReturn(Fixture.order(1L));
        when(orderItemRepository.create(any())).thenReturn(orderSheet.getOrderItems());
        Order order = sut.createOrder(orderSheet);

        //then
        assert order != null;
        assert order.getUserId() == orderSheet.getUserId();
        assert order.getOrderStatus() == PENDING;
        assert order.getOrderItems().size() == orderSheet.getOrderItems().size();
        assert order.getTotalPrice().compareTo(orderSheet.getTotalPrice()) == 0;
    }

    @Test
    @DisplayName("요청 상태가 PAID일 때, 현재 주문 상태가 PENDING이 아닌 경우 INVALID_ORDER_STATUS 예외 발생")
    void updateOrderStatus_fail_INVALID_ORDER_STATUS_1() {
        // given
        OrderStatus request = OrderStatus.PAID;
        Order order = Fixture.order(1L);
        order.setOrderStatus(COMPLETED);

        // when
        Throwable exception = null;
        try {
            sut.updateOrderStatus(order, request);
        } catch (EcommerceException e) {
            exception = e;
        }

        // then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_ORDER_STATUS;
    }

    @Test
    @DisplayName("요청 상태가 COMPLETED일 때, 현재 주문 상태가 PAID가 아닌 경우 INVALID_ORDER_STATUS 예외 발생")
    void updateOrderStatus_fail_INVALID_ORDER_STATUS_2() {
        // given
        OrderStatus request = OrderStatus.COMPLETED;
        Order order = Fixture.order(1L);
        order.setOrderStatus(PENDING);

        // when
        Throwable exception = null;
        try {
            sut.updateOrderStatus(order, request);
        } catch (EcommerceException e) {
            exception = e;
        }

        // then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_ORDER_STATUS;
    }

    @Test
    @DisplayName("현재 주문 상태가 PENDING일 때, 요청 상태가 PAID인 경우 주문 상태 업데이트 성공")
    void updateOrderStatus_success_1() {
        // given
        OrderStatus request = OrderStatus.PAID;
        Order order = Fixture.order(1L);
        order.setOrderStatus(PENDING);

        // when
        sut.updateOrderStatus(order, request);

        // then
        verify(orderRepository, atLeastOnce()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("요청 상태가 COMPLETED일 때, 현재 주문 상태가 PAID인 경우 주문 상태 업데이트 성공")
    void updateOrderStatus_success_2() {
        // given
        OrderStatus request = OrderStatus.COMPLETED;
        Order order = Fixture.order(1L);
        order.setOrderStatus(PAID);

        // when
        sut.updateOrderStatus(order, request);

        // then
        verify(orderRepository, atLeastOnce()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("주문 상품 상태 업데이트 성공")
    void updateOrderItemStatus_success() {
        // given
        List<OrderItem> orderItems = Fixture.orderItems(2);
        OrderItemStatus status = OrderItemStatus.CANCELED;
        // when
        sut.updateOrderItemStatus(orderItems, status);

        // then
        verify(orderItemRepository, atLeastOnce()).updateStatus(any(), any());
    }
}