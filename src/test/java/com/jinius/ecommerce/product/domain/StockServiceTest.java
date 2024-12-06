package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.RLockHandler;
import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.common.exception.LockException;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.product.domain.model.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @InjectMocks
    StockService sut;

    @Mock
    RLockHandler lockHandler;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("재고 차감 요청 시 재고 조회가 Optional.empty()인 경우 NOT_FOUND_PRODUCT 예외 발생")
    void decreaseStock_NOT_FOUND_PRODUCT_failed() {
        //given
        List<OrderItem> orderItems = Fixture.orderItems(3);

        //when
        when(productRepository.findStockById(any(Long.class))).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(1);
            runnable.run();
            return null;
        }).when(lockHandler).callWithLock(
                any(String.class), any(Runnable.class), any(Long.class), any(Long.class), any(TimeUnit.class)
        );

        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException | LockException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_FOUND_PRODUCT;
    }
    @Test

    @DisplayName("재고 차감 요청 시 요청한 주문 수량이 재고보다 많은 경우, NOT_ENOUGH_STOCK 예외 발생")
    void decreaseStock_NOT_ENOUGH_STOCK_failed() {
        //given
        List<OrderItem> orderItems = Fixture.orderItems();
        orderItems.get(0).setQuantity(10000L);

        //when
        when(productRepository.findStockById(any(Long.class))).thenReturn(Optional.of(Fixture.stockOne()));
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(1);
            runnable.run();
            return null;
        }).when(lockHandler).callWithLock(
                any(String.class), any(Runnable.class), any(Long.class), any(Long.class), any(TimeUnit.class)
        );

        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException | LockException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_ENOUGH_STOCK;
    }

    @Test
    @DisplayName("재고 차감 요청 시 요청한 주문 수량만큼 재고를 차감한 후 DB에 저장을 성공한 경우 예외가 발생하지 않는다.")
    void decreaseStock_success() {
        //given
        List<OrderItem> orderItems = Fixture.orderItem(); //수량 1개
        Stock dbStock = Fixture.stockOne(); //수량 50개

        //when
        when(productRepository.findStockById(any(Long.class))).thenReturn(Optional.of(dbStock));
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(1);
            runnable.run();
            return null;
        }).when(lockHandler).callWithLock(
                any(String.class), any(Runnable.class), any(Long.class), any(Long.class), any(TimeUnit.class)
        );

        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException | LockException e) {
            exception = e;
        }

        //then
        assert exception == null;
        // 재고 저장 로직 호출 확인
        verify(productRepository).updateStock(dbStock);
    }
}