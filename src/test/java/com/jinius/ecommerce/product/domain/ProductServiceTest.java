package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.product.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService sut;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("인기 상위 5개 상품 조회 시 빈 값일 경우 NOT_FOUND_PRODUCT 예외 발생")
    void getTop5Products_NOT_FOUND_PRODUCT_failed() {
        //given, when
        when(productRepository.findTop5ItemsLast3Days(any(), any(), any())).thenReturn(Collections.emptyList());

        Throwable exception = null;
        try {
            sut.getTop5Products();
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_FOUND_PRODUCT;
    }

    @Test
    @DisplayName("인기 상위 5개 상품 조회 시 5개 이상의 상품이 조회된 경우 EXCEED_COUNT 예외 발생")
    void getTop5Products_EXCEED_COUNT_failed() {
        //given, when
        when(productRepository.findTop5ItemsLast3Days(any(), any(), any())).thenReturn(Fixture.ExceedProductsList());

        Throwable exception = null;
        try {
            sut.getTop5Products();
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.EXCEED_COUNT;
    }

    @Test
    @DisplayName("인기 상위 5개 상품 조회 성공")
    void getTop5Products_success() {
        //given, when
        when(productRepository.findTop5ItemsLast3Days(any(), any(), any())).thenReturn(Fixture.top5ProductList());

        List<Product> top5Products = sut.getTop5Products();

        //then
        assert top5Products.size() == 5;
    }
}