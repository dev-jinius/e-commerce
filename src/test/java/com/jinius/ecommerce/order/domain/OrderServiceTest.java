package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.api.OrderItemRequest;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.order.infra.StubOrderRepository;
import com.jinius.ecommerce.payment.domain.StubPaymentService;
import com.jinius.ecommerce.product.domain.StubProductService;
import com.jinius.ecommerce.user.domain.StubUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

class OrderServiceTest {

    private final OrderService sut;
    private final StubUserService stubUserService;
    private final StubPaymentService stubPaymentService;
    private final StubProductService stubProductService;
    private final StubOrderRepository stubOrderRepository;

    OrderServiceTest() {
        stubUserService = new StubUserService();
        stubPaymentService = new StubPaymentService();
        stubProductService = new StubProductService();
        stubOrderRepository = new StubOrderRepository();
        this.sut = new OrderService(stubUserService, stubPaymentService, stubProductService, stubOrderRepository);
    }

    @Test
    @DisplayName("주문서 생성 시 존재하지 않는 유저의 요청으로 주문 실패")
    void createOrder_userNotFound_failed() {
        //given
        OrderRequest request = new OrderRequest(
            100L,
            List.of(
                new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L)
            )
        );

        //when
        Throwable exception = null;
        try {
            sut.createOrder(request);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof EcommerceException;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_FOUND_USER;
        assert ((EcommerceException) exception).getErrorCode().getMessage().equals("존재하지 않는 유저입니다.");
    }

    @Test
    @DisplayName("유저가 존재하는 경우 주문서 생성 성공")
    void createOrder_existingUser_success() {
        //given
        OrderRequest request = new OrderRequest(
                1L,
                List.of(
                    new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L)
                )
        );

        // when & then
        Order order = sut.createOrder(request);

        assert order != null;
        assert order.getUserId() == 1L;
        assert order.getPaymentType().equals("POINT");
    }

    @Test
    @DisplayName("주문 요청 시 포인트 부족으로 주문 실패")
    void createOrder_pointNotEnough_failed() {
        //given
        OrderRequest request = new OrderRequest(
                1L,
                List.of(
                        new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L),
                        new OrderItemRequest(3L, BigInteger.valueOf(30000), 2L)
                )
        );

        //when
        Throwable exception = null;
        try {
            sut.createOrder(request);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof EcommerceException;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_ENOUGH_POINT;
    }

    @Test
    @DisplayName("주문 요청 시 포인트 잔액이 주문 금액보다 많은 경우 주문서 생성 성공")
    void createOrder_enoughPoint_success() {
        //given
        OrderRequest request = new OrderRequest(
                1L,
                List.of(
                        new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L)
                )
        );

        // when & then
        Order order = sut.createOrder(request);

        assert order != null;
        assert order.getUserId() == 1L;
        assert order.getPaymentType().equals("POINT");
    }
}