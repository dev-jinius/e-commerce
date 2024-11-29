package com.jinius.ecommerce.order.application;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.order.api.dto.OrderItemRequest;
import com.jinius.ecommerce.order.api.dto.OrderRequest;
import com.jinius.ecommerce.order.domain.OrderService;
import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderItemStatus;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.order.domain.model.OrderStatus;
import com.jinius.ecommerce.payment.domain.model.Payment;
import com.jinius.ecommerce.payment.domain.PaymentService;
import com.jinius.ecommerce.payment.domain.model.PaymentType;
import com.jinius.ecommerce.user.domain.model.User;
import com.jinius.ecommerce.user.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static com.jinius.ecommerce.common.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 주문 통합 테스트
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderFacadeTest {

    @Autowired
    private OrderFacade sut;

    @MockBean
    private OrderService orderService;
    @MockBean
    private UserService userService;
    @MockBean
    private PaymentService paymentService;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest(
                1L,
                List.of(new OrderItemRequest(
                        1L,
                        BigInteger.valueOf(49900),
                        1L
                ))
        );
    }

    @Test
    @DisplayName("주문 요청 시 존재하지 않는 유저인 경우 NOT_FOUND_USER 예외 발생")
    void order_fail_NOT_FOUND_USER() {
        //given
        given(userService.getUser(any(Long.class)))
                .willThrow(new EcommerceException(NOT_FOUND_USER));

        //when
        Throwable exception = null;
        try {
            sut.order(orderRequest.toFacade());
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
    @DisplayName("주문 요청 시 주문 생성 실패로 FAIL_CREATE_ORDER 예외 발생")
    void order_fail_FAIL_CREATE_ORDER() {
        //given
        Long userId = 1L;
        User mockUser = User.builder()
                .userId(userId)
                .name("Iris")
                .point(BigInteger.valueOf(100000))
                .build();
        OrderSheet mockOrderSheet = Fixture.orderSheet(userId);

        given(userService.getUser(any())).willReturn(mockUser);
        given(orderService.createOrderSheet(any())).willReturn(mockOrderSheet);
        given(orderService.createOrder(any())).willReturn(null);
        given(orderService.createOrder(any(OrderSheet.class)))
                .willThrow(new EcommerceException(FAIL_CREATE_ORDER));

        //when
        Throwable exception = null;
        try {
            sut.order(orderRequest.toFacade());
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof EcommerceException;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.FAIL_CREATE_ORDER;
    }

    @Test
    @DisplayName("잔액 포인트 부족으로 결제 정보 생성 실패 시 주문/주문상품 상태 업데이트 후 FAIL_CREATE_ORDER 예외 발생으로 주문 취소")
    void order_fail_FAIL_NOT_ENOUGH_POINT() {
        //given
        Long userId = 1L;
        User mockUser = User.builder()
                .userId(userId)
                .name("Iris")
                .point(BigInteger.valueOf(100000))
                .build();
        OrderSheet mockOrderSheet = Fixture.orderSheet(userId);
        Order mockOrder = Fixture.order(userId);

        given(userService.getUser(any())).willReturn(mockUser);
        given(orderService.createOrderSheet(any())).willReturn(mockOrderSheet);
        given(orderService.createOrder(any())).willReturn(mockOrder);
        given(paymentService.createPayment(any()))
                .willThrow(new EcommerceException(NOT_ENOUGH_POINT));

        //when
        Throwable exception = null;
        try {
            sut.order(orderRequest.toFacade());
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof EcommerceException;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_ENOUGH_POINT;
    }

    @Test
    @DisplayName("주문 성공 시 주문 상태가 COMPLETED 상태로 변경되고, 유저 포인트가 차감된다.")
    void order_success() {
        //given
        Long userId = 1L;
        User mockUser = Fixture.user(userId, 100000);
        OrderSheet mockOrderSheet = Fixture.orderSheet(userId);
        Order mockOrder = Fixture.order(userId);
        Payment mockPayment = Fixture.payment(Fixture.orderPaymentInfoForPoint(mockUser, mockOrder, PaymentType.POINT));

        given(userService.getUser(any())).willReturn(mockUser);
        given(orderService.createOrderSheet(any())).willReturn(mockOrderSheet);
        given(orderService.createOrder(any())).willReturn(mockOrder);
        given(paymentService.createPayment(any())).willReturn(mockPayment);

        //when
        sut.order(orderRequest.toFacade());

        //then
        User result = userService.getUser(userId);
        System.out.println("result.getPoint() = " + result.getPoint());
        System.out.println("mockUser.getPoint() = " + mockUser.getPoint());
        System.out.println("mockPayment.getPoint() = " + mockPayment.getPoint());
        System.out.println(mockOrderSheet.getTotalPrice());
        verify(orderService).updateOrderStatus(mockOrder, OrderStatus.COMPLETED);
        assert result.getPoint().compareTo(BigInteger.valueOf(100000).subtract(mockOrderSheet.getTotalPrice())) == 0;
    }
}
