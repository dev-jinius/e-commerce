package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService sut;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("재고 차감 요청 시 orderItems 파라미터 개수가 0인 경우 INVALID_PARAMETER 예외 발생")
    void decreaseStock_invalidOrderItems_failed() {
        //given
        List<OrderItem> orderItems = null;

        //when
        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
    }


    @Test
    @DisplayName("재고 차감 요청 시 재고 조회가 Optional.empty()인 경우 NOT_FOUND_PRODUCT 예외 발생")
    void decreaseStock_NOT_FOUND_PRODUCT_failed() {
        //given
        List<OrderItem> orderItems = Fixture.requestDecreaseStockForId1One();

        //when
        when(productRepository.findStockById(any(Long.class))).thenReturn(Optional.empty());
        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException e) {
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
        List<OrderItem> orderItems = Fixture.requestDecreaseStockForId1One();
        orderItems.get(0).setQuantity(10000L);

        //when
        when(productRepository.findStockById(any(Long.class))).thenReturn(Optional.of(Fixture.stockOne()));
        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException e) {
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
        List<OrderItem> orderItems = Fixture.requestDecreaseStockForId1One(); //수량 1개
        Stock dbStock = Fixture.stockOne(); //수량 50개

        //when
        when(productRepository.findStockById(any(Long.class))).thenReturn(Optional.of(dbStock));
        Throwable exception = null;
        try {
            sut.decreaseStock(orderItems);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception == null;
    }
}