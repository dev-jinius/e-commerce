package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.api.OrderItemRequest;
import com.jinius.ecommerce.order.api.OrderRequest;
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

    OrderServiceTest() {
        stubUserService = new StubUserService();
        this.sut = new OrderService(stubUserService);
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
                    new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L),
                    new OrderItemRequest(3L, BigInteger.valueOf(30000), 2L)
                )
        );

        // when & then
        OrderSheet orderSheet = sut.createOrder(request);

        assert orderSheet != null;
        assert orderSheet.getUserId() == 1L;
        assert orderSheet.getPaymentType().equals("POINT");
    }
}